package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.joda.time.DateTime;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    protected Session session;

    @Override
    @Transactional("neo4jTransactionManager")
    public List<SearchResult> findPathByDepartureDate(String stopFromName, String stopToName, String stopThroughName,
                                                      DateTime departure, int maxHoursAfterDeparture, int maxTransfers, boolean withWheelChair) {
        final Map<String, Object> params = new HashMap<>();
        params.put("from", stopFromName);
        params.put("to", stopThroughName != null ? stopThroughName : stopToName);
        params.put("departure", departure.getMillis());
        params.put("maxDeparture", new DateTime(departure).plusHours(maxHoursAfterDeparture).getMillis());
        params.put("maxTransfers", maxTransfers);
        params.put("maxNumberOfResults", DEFAULT_MAX_NUMBER_OF_RESULTS);
        params.put("wheelChair", withWheelChair);
        params.put("stopToWheelChairAccessible", withWheelChair);
        params.put("stopTimeId", null);

        final String query = "CALL cz.cvut.dp.nss.search.byDepartureSearch({from}, {to}, {departure}, {maxDeparture}, {maxTransfers}, {maxNumberOfResults}, {wheelChair}, {stopToWheelChairAccessible}, {stopTimeId})";
        Result result = session.query(query, params, true);

        List<SearchResult> retList = new ArrayList<>();
        for(Map<String, Object> searchResultMap : result) {
            retList.add(buildSearchResultWrapperFromMap(searchResultMap));
        }

        //TODO muze se stat, ze vracene vysledky maji nejaky stejny TRIP - to je treba zkontrolovat a pokud to nastane
        //TODO tak ten druhy smazat a najit dalsi vysledky, aby byly celkem 3

        //TODO taky muze nastat situace Cervenanskeho -> Dejvicka pres Nove Butovice s 1 prestupem v 09:00
        //TODO najde to jen jeden vysledek, protoze prvotni hledani na butovice najde 184, 142, 142
        //TODO ty 142 ale konci na butovicich a je nutne dale prestoupit, cimz si vybereme prestup, ktery je pak treba
        //TODO bud v nemocnici motol nebo na mustku. Spravne by to melo najit tri spoje kdy vsechny zacinaji 184 a jedou az do nemocnice motol
        if(stopThroughName != null) {
            //vyse nalezene vysledky jsou pouze do prujezdni/prestupni stanice. Nyni budu hledat spojeni dale do koncove
            //navic budu prizpusobovat parametry
            List<SearchResult> retListForWheelChairs = new ArrayList<>();
            if(withWheelChair) {
                //pokud vyhledavam s prujezdni stanici tak musim najit vysledky start -> prujezd znovu
                //tentokrat s atributem, ze koncova stanice (tedy prujezdni z vyhledavani) nemusi byt bezbarierova
                //je to proto, ze kdyz na ni nebudu prestupovat ale zustanu ve voze, tak opravdu nemu byt bezbarierova
                params.put("stopToWheelChairAccessible", false);
                Result resultForWheelChairs = session.query(query, params, true);

                for(Map<String, Object> searchResultMap : resultForWheelChairs) {
                    retListForWheelChairs.add(buildSearchResultWrapperFromMap(searchResultMap));
                }
            }

            params.put("stopToWheelChairAccessible", withWheelChair);
            params.put("from", stopThroughName);
            params.put("to", stopToName);
            params.put("maxNumberOfResults", 1);

            List<SearchResult> thisRetList = new ArrayList<>();
            //a pro kazdy vysledek do prestupni stanice spustim vyhledavani do cilove
            for(int i = 0; i < Math.max(retList.size(), retListForWheelChairs.size()); i++) {
                SearchResult searchResult;

                //nejdrive se pokusim najit vysledek bez prestupu na prujezdni stanici
                //tedy search result beru z tech, ktere nemusi mit koncovou stanici bezbarierovou
                searchResult = retListForWheelChairs.size() > i ? retListForWheelChairs.get(i) : retList.get(i);
                DateTime thisDeparture = getThisDeparture(searchResult, departure);

                //stopTimeId bude posledni nalezeny stopTime zatim - budu hledat primo od nej
                fillParams(params, searchResult.getStopTimes().get(searchResult.getStopTimes().size() - 1), thisDeparture.getMillis(), maxTransfers - searchResult.getNumberOfTransfers());
                SearchResult directSearchResult = null;
                result = session.query(query, params, true);
                for(Map<String, Object> searchResultMap : result) {
                    directSearchResult = buildSearchResultWrapperFromMap(searchResultMap);
                    break;
                }

                //a potom vysledek s prestupem na prujezdni stanici
                SearchResult transferedSearchResult = null;
                //hledat s prestupem muzu jen pokud mam alespon jeden prestup jeste k dispozici
                if(maxTransfers - searchResult.getNumberOfTransfers() > 0 && retList.size() > i) {
                    //budu hledat oproti vysledku, ktery musi mit koncovou (nasi prestupni) stanici bezbarierovou, pokud vyhledavam bezbarierove
                    //to proto, ze na te prestupni musim vystoupit a nastoupit do jineho vozu
                    searchResult = retList.get(i);
                    //upravim si params
                    fillParams(params, null, thisDeparture.plusSeconds(DateTimeUtils.MIN_TRANSFER_SECONDS).getMillis(), maxTransfers - searchResult.getNumberOfTransfers() - 1);
                    result = session.query(query, params, true);
                    for(Map<String, Object> searchResultMap : result) {
                        transferedSearchResult = buildSearchResultWrapperFromMap(searchResultMap);
                        break;
                    }
                }

                //z vysledku vyberu ten lepsi (drivejsi v cilove stanici)
                SearchResult thisSearchResult;
                if(directSearchResult != null && transferedSearchResult != null) {
                    //osetreni pulnoci - pokud je direct pred pulnoci a transfered po pulnoci nebo pokud je direct mensi tak beru ten, jinak transfered
                    final boolean directOverMidnight = searchResult.getDeparture() > directSearchResult.getArrival();
                    final boolean transferedOverMidnight = searchResult.getDeparture() > transferedSearchResult.getArrival();
                    if((transferedOverMidnight && !directOverMidnight) || directSearchResult.getArrival() <= transferedSearchResult.getArrival()) {
                        thisSearchResult = directSearchResult;
                    } else {
                        thisSearchResult = transferedSearchResult;
                    }
                } else if(directSearchResult != null) {
                    thisSearchResult = directSearchResult;
                } else if(transferedSearchResult != null) {
                    thisSearchResult = transferedSearchResult;
                } else {
                    //zadny vysledek nenalezen
                    continue;
                }

                //ted musim oboje zmergovat do jednoho vysledku - tedy start -> prestup a prestup -> end
                SearchResult ret = mergeResults(searchResult, thisSearchResult);
                thisRetList.add(ret);
            }

            return thisRetList;
        }

        return retList;
    }

    /**
     * @param searchResult vysledek vyhledavani start -> prestupni stanice
     * @param thisSearchResult vysledek vyhledavani prestupni stanice -> end
     * @return zmergovane vysledky searchResult a thisSearchResult (spojene prestupni stanici)
     */
    private SearchResult mergeResults(SearchResult searchResult, SearchResult thisSearchResult) {
        SearchResult ret = new SearchResult();
        ret.setDeparture(searchResult.getDeparture());
        ret.setArrival(thisSearchResult.getArrival());
        final long travelTime;
        if(thisSearchResult.getArrival() > searchResult.getDeparture()) {
            //v ramci dne
            travelTime = thisSearchResult.getArrival() - searchResult.getDeparture();
        } else {
            //pres pulnoc
            travelTime = DateTimeUtils.SECONDS_IN_DAY - searchResult.getDeparture() + thisSearchResult.getArrival();
        }
        ret.setTravelTime(travelTime);
        ret.setNumberOfTransfers(searchResult.getNumberOfTransfers() + thisSearchResult.getNumberOfTransfers() + 1);
        ret.setOverMidnightDeparture(searchResult.isOverMidnightDeparture());
        ret.setOverMidnightArrival(searchResult.isOverMidnightDeparture() || thisSearchResult.isOverMidnightArrival() || thisSearchResult.getArrival() < searchResult.getDeparture());

        List<Long> stopTimes = new ArrayList<>(searchResult.getStopTimes());
        List<Long> thisStopTimes = new ArrayList<>(thisSearchResult.getStopTimes());

        //pokud jsem na through stanici pouze projel jednim spojem tak ji nechci ve vysledcich
        if(stopTimes.get(stopTimes.size() - 1).equals(thisStopTimes.get(0))) {
            stopTimes.remove(stopTimes.size() - 1);
            thisStopTimes.remove(0);
        }

        stopTimes.addAll(thisStopTimes);
        ret.setStopTimes(stopTimes);

        return ret;
    }

    /**
     * @param searchResult vysledek vyhledavani
     * @param departure caj vyjezdu od
     * @return spravny datum ziskany ze searchResult.getArrival a naroubovany na den departure (tedy vteriny arrival se nastavi do dne departure)
     */
    private DateTime getThisDeparture(SearchResult searchResult, DateTime departure) {
        DateTime thisDeparture;
        if(!searchResult.isOverMidnightArrival()) {
            thisDeparture = new DateTime(departure);
        } else {
            thisDeparture = new DateTime(departure).plusDays(1);
        }

        return thisDeparture.withMillisOfDay((int) searchResult.getArrival() * 1000);
    }

    /**
     * naplni mapu parametry
     * @param params mapa parametru
     * @param stopTimeId id stopTime
     * @param departure vyjezd
     * @param maxTransfers max pocet prestupu
     */
    private void fillParams(Map<String, Object> params, Long stopTimeId, long departure, int maxTransfers) {
        params.put("stopTimeId", stopTimeId);
        params.put("departure", departure);
        params.put("maxTransfers", maxTransfers);
    }

    private static SearchResult buildSearchResultWrapperFromMap(Map<String, Object> map) {
        SearchResult wrapper = new SearchResult();
        wrapper.setDeparture((long) map.get("departure"));
        wrapper.setArrival((long) map.get("arrival"));
        wrapper.setTravelTime((long) map.get("travelTime"));
        wrapper.setNumberOfTransfers(((Long) map.get("numberOfTransfers")).intValue());
        wrapper.setOverMidnightDeparture((boolean) map.get("overMidnightDeparture"));
        wrapper.setOverMidnightArrival((boolean) map.get("overMidnightArrival"));
        wrapper.setStopTimes(Arrays.asList((Long[]) map.get("stops")));

        return wrapper;
    }

}
