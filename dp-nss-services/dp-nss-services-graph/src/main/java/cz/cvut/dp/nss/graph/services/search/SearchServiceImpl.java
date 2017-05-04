package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResultByDepartureDateComparator;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Implementace SearchService.
 *
 * @author jakubchalupa
 * @since 11.02.17
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private Session session;

    @Resource(name = "searchServiceImpl")
    private SearchService searchService;

    @Override
    @Transactional(value = "neo4jTransactionManager", readOnly = true)
    public List<SearchResult> findPathByDepartureDate(String stopFromName, String stopToName, String stopThroughName,
                                                      DateTime departure, DateTime maxDeparture, int maxTransfers, int maxNumberOfResults,
                                                      boolean withWheelChair, List<SearchResult> alreadyFoundedSearchResultsWithThroughStop) {
        final Map<String, Object> params = new HashMap<>();
        params.put("from", stopFromName);
        params.put("to", stopThroughName != null ? stopThroughName : stopToName);
        params.put("departure", departure.getMillis());
        params.put("maxDeparture", maxDeparture.getMillis());
        params.put("maxTransfers", maxTransfers);
        params.put("maxNumberOfResults", maxNumberOfResults);
        params.put("wheelChair", withWheelChair);
        params.put("stopToWheelChairAccessible", withWheelChair);
        params.put("stopTimeId", null);

        final String query = "CALL cz.cvut.dp.nss.search.byDepartureSearch({from}, {to}, {departure}, {maxDeparture}, {maxTransfers}, {maxNumberOfResults}, {wheelChair}, {stopToWheelChairAccessible}, {stopTimeId})";
        Result result = session.query(query, params, true);

        List<SearchResult> retList = new ArrayList<>();
        for(Map<String, Object> searchResultMap : result) {
            retList.add(buildSearchResultWrapperFromMap(searchResultMap));
        }

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

            //vyberu zatim nejvyssi nalezeny departure z vychozi stanice
            //pro ucely vyhledavani s prestupem, pokud bych v prvni iteraci nenasel dostatecny pocet vysledku
            DateTime currentMaxDepartureFromStart = new DateTime(0);
            for(SearchResult searchResult : retList) {
                DateTime thisDateTime = getThisDeparture(searchResult, departure);
                if(thisDateTime.isAfter(currentMaxDepartureFromStart)) {
                    currentMaxDepartureFromStart = thisDateTime;
                }
            }
            for(SearchResult searchResult : retListForWheelChairs) {
                DateTime thisDateTime = getThisDeparture(searchResult, departure);
                if(thisDateTime.isAfter(currentMaxDepartureFromStart)) {
                    currentMaxDepartureFromStart = thisDateTime;
                }
            }

            params.put("stopToWheelChairAccessible", withWheelChair);
            params.put("from", stopThroughName);
            params.put("to", stopToName);
            params.put("maxNumberOfResults", 1);

            List<SearchResult> thisRetList = alreadyFoundedSearchResultsWithThroughStop != null ? alreadyFoundedSearchResultsWithThroughStop : new ArrayList<>();
            //a pro kazdy vysledek do prestupni stanice spustim vyhledavani do cilove
            for(int i = 0; i < Math.max(retList.size(), retListForWheelChairs.size()); i++) {
                SearchResult searchResult;

                //nejdrive se pokusim najit vysledek bez prestupu na prujezdni stanici
                //tedy search result beru z tech, ktere nemusi mit koncovou stanici bezbarierovou
                searchResult = retListForWheelChairs.size() > i ? retListForWheelChairs.get(i) : retList.get(i);
                DateTime thisDeparture = getThisArrival(searchResult, departure);

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
                    thisDeparture = getThisArrival(searchResult, departure);
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
                    if((transferedOverMidnight && !directOverMidnight) || directSearchResult.getArrival() < transferedSearchResult.getArrival()) {
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
                SearchResult ret = mergeResults(searchResult, thisSearchResult, false);

                //kouknu, jestli s predchozim vysledkem nemam nejaky spolecny stopTime, pokud ano, tak ten minuly smazu a nahraju jen tento
                if(!thisRetList.isEmpty()) {
                    SearchResult prevSearchResult = thisRetList.get(thisRetList.size() - 1);
                    Set<Long> prevStopTimes = new HashSet<>(prevSearchResult.getStopTimes());
                    Set<Long> thisStopTimes = new HashSet<>(ret.getStopTimes());

                    if(!Collections.disjoint(prevStopTimes, thisStopTimes)) {
                        //mam nejaky spolecny stopTime, ten horsi vysledek tedy smazu
                        if(new SearchResultByDepartureDateComparator().compare(prevSearchResult, ret) > 0) {
                            //lepsi je ten novy, takze ten minuly musim smazat a pridat ten novy
                            thisRetList.remove(thisRetList.size() - 1);
                            thisRetList.add(ret);
                        }
                    } else {
                        thisRetList.add(ret);
                    }
                } else {
                    thisRetList.add(ret);
                }

            }

            //pokud neni nyni thisRetList plny na pozadovany pocet, tak je treba nejake vysledky doplnit
            //zavolame rekurzivne vyhledavani s posunutym casem odjezdu a dalsimi upravenymi atributy
            if(!thisRetList.isEmpty() && thisRetList.size() < SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS) {

                //departure bude o vterinu vyssi, nez byl dozatim nalezeny vyjezd ze stopFromName
                DateTime nextDeparture = currentMaxDepartureFromStart.plusSeconds(1);
                if(nextDeparture.isBefore(maxDeparture)) {
                    //a rekurzivne volam jen pokud mam jeste nejaky cas k hledani
                    return searchService.findPathByDepartureDate(stopFromName, stopToName, stopThroughName, nextDeparture, maxDeparture,
                        maxTransfers, SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS - thisRetList.size(), withWheelChair, thisRetList);
                }
            }

            return thisRetList;
        }

        return retList;
    }

    @Override
    @Transactional(value = "neo4jTransactionManager", readOnly = true)
    public List<SearchResult> findPathByArrivalDate(String stopFromName, String stopToName, String stopThroughName, DateTime arrival, DateTime minArrival, int maxTransfers, int maxNumberOfResults, boolean withWheelChair, List<SearchResult> alreadyFoundedSearchResultsWithThroughStop) {
        final Map<String, Object> params = new HashMap<>();
        params.put("from", stopThroughName != null ? stopThroughName : stopFromName);
        params.put("to", stopToName);
        params.put("arrival", arrival.getMillis());
        params.put("minArrival", minArrival.getMillis());
        params.put("maxTransfers", maxTransfers);
        params.put("maxNumberOfResults", maxNumberOfResults);
        params.put("wheelChair", withWheelChair);
        params.put("stopFromWheelChairAccessible", withWheelChair);
        params.put("stopTimeId", null);

        final String query = "CALL cz.cvut.dp.nss.search.byArrivalSearch({from}, {to}, {arrival}, {minArrival}, {maxTransfers}, {maxNumberOfResults}, {wheelChair}, {stopFromWheelChairAccessible}, {stopTimeId})";
        Result result = session.query(query, params, true);

        List<SearchResult> retList = new ArrayList<>();
        for(Map<String, Object> searchResultMap : result) {
            retList.add(buildSearchResultWrapperFromMap(searchResultMap));
        }

        if(stopThroughName != null) {
            //vyse nalezene vysledky jsou pouze do prujezdni/prestupni stanice. Nyni budu hledat spojeni dale do koncove
            //navic budu prizpusobovat parametry
            List<SearchResult> retListForWheelChairs = new ArrayList<>();
            if(withWheelChair) {
                //pokud vyhledavam s prujezdni stanici tak musim najit vysledky start -> prujezd znovu
                //tentokrat s atributem, ze koncova stanice (tedy prujezdni z vyhledavani) nemusi byt bezbarierova
                //je to proto, ze kdyz na ni nebudu prestupovat ale zustanu ve voze, tak opravdu nemusi byt bezbarierova
                params.put("stopFromWheelChairAccessible", false);
                Result resultForWheelChairs = session.query(query, params, true);

                for(Map<String, Object> searchResultMap : resultForWheelChairs) {
                    retListForWheelChairs.add(buildSearchResultWrapperFromMap(searchResultMap));
                }
            }

            //vyberu zatim nejnizsi nalezeny arrival do cilove stanice
            //pro ucely vyhledavani s prestupem, pokud bych v prvni iteraci nenasel dostatecny pocet vysledku
            DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeUtils.DATE_TIME_PATTERN);
            DateTime currentMinArrivalFromStart = formatter.parseDateTime("01.01.2099 15:00");
            for(SearchResult searchResult : retList) {
                DateTime thisDateTime = getThisArrivalForArrival(searchResult, arrival);
                if(thisDateTime.isBefore(currentMinArrivalFromStart)) {
                    currentMinArrivalFromStart = thisDateTime;
                }
            }
            for(SearchResult searchResult : retListForWheelChairs) {
                DateTime thisDateTime = getThisArrivalForArrival(searchResult, arrival);
                if(thisDateTime.isBefore(currentMinArrivalFromStart)) {
                    currentMinArrivalFromStart = thisDateTime;
                }
            }

            params.put("stopFromWheelChairAccessible", withWheelChair);
            params.put("from", stopFromName);
            params.put("to", stopThroughName);
            params.put("maxNumberOfResults", 1);

            List<SearchResult> thisRetList = alreadyFoundedSearchResultsWithThroughStop != null ? alreadyFoundedSearchResultsWithThroughStop : new ArrayList<>();
            //a pro kazdy vysledek z prestupni do cilove stanice spustim vyhledavani z vychozi do prestupni
            for(int i = Math.max(retList.size(), retListForWheelChairs.size()) - 1; i >= 0; i--) {
                SearchResult searchResult;

                //nejdrive se pokusim najit vysledek bez prestupu na prujezdni stanici
                //tedy search result beru z tech, ktere nemusi mit koncovou stanici bezbarierovou
                searchResult = retListForWheelChairs.size() > i ? retListForWheelChairs.get(i) : retList.get(i);
                DateTime thisArrival = getThisDepartureForArrival(searchResult, arrival);

                //stopTimeId bude prvni nalezeny stopTime zatim - budu hledat primo do nej
                fillArrivalParams(params, searchResult.getStopTimes().get(0), thisArrival.getMillis(), maxTransfers - searchResult.getNumberOfTransfers());
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
                    thisArrival = getThisDepartureForArrival(searchResult, arrival);
                    fillArrivalParams(params, null, thisArrival.minusSeconds(DateTimeUtils.MIN_TRANSFER_SECONDS).getMillis(), maxTransfers - searchResult.getNumberOfTransfers() - 1);
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
                    final boolean directOverMidnight = searchResult.getArrival() < directSearchResult.getDeparture();
                    final boolean transferedOverMidnight = searchResult.getArrival() < transferedSearchResult.getDeparture();
                    if((transferedOverMidnight && !directOverMidnight) || directSearchResult.getDeparture() > transferedSearchResult.getDeparture()) {
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
                SearchResult ret = mergeResults(thisSearchResult, searchResult, true);

                //kouknu, jestli s predchozim vysledkem nemam nejaky spolecny stopTime, pokud ano, tak ten minuly smazu a nahraju jen tento
                if(!thisRetList.isEmpty()) {
                    SearchResult prevSearchResult = thisRetList.get(0);
                    Set<Long> prevStopTimes = new HashSet<>(prevSearchResult.getStopTimes());
                    Set<Long> thisStopTimes = new HashSet<>(ret.getStopTimes());

                    if(!Collections.disjoint(prevStopTimes, thisStopTimes)) {
                        //mam nejaky spolecny stopTime, ten horsi vysledek tedy smazu
                        if(new SearchResultByDepartureDateComparator().compare(prevSearchResult, ret) > 0) {
                            //lepsi je ten novy, takze ten minuly musim smazat a pridat ten novy
                            thisRetList.remove(0);
                            thisRetList.add(0, ret);
                        }
                    } else {
                        thisRetList.add(0, ret);
                    }
                } else {
                    thisRetList.add(0, ret);
                }

            }

            //pokud neni nyni thisRetList plny na pozadovany pocet, tak je treba nejake vysledky doplnit
            //zavolame rekurzivne vyhledavani s posunutym casem prijezdu a dalsimi upravenymi atributy
            if(!thisRetList.isEmpty() && thisRetList.size() < SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS) {

                //arrival bude o vterinu nizsi, nez byl dozatim nalezeny prijezd do stopToName
                DateTime nextArrival = currentMinArrivalFromStart.minusSeconds(1);
                if(nextArrival.isAfter(minArrival)) {
                    //a rekurzivne volam jen pokud mam jeste nejaky cas k hledani
                    return searchService.findPathByArrivalDate(stopFromName, stopToName, stopThroughName, nextArrival, minArrival,
                        maxTransfers, SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS - thisRetList.size(), withWheelChair, thisRetList);
                }
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
    private SearchResult mergeResults(SearchResult searchResult, SearchResult thisSearchResult, boolean byArrival) {
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
        if(!byArrival) {
            ret.setOverMidnightDeparture(searchResult.isOverMidnightDeparture());
            ret.setOverMidnightArrival(searchResult.isOverMidnightArrival() || thisSearchResult.isOverMidnightArrival() || thisSearchResult.getArrival() < searchResult.getDeparture());
        } else {
            ret.setOverMidnightArrival(thisSearchResult.isOverMidnightArrival());
            ret.setOverMidnightDeparture(searchResult.isOverMidnightDeparture() || thisSearchResult.isOverMidnightDeparture() || searchResult.getDeparture() > thisSearchResult.getArrival());
        }

        List<Long> stopTimes = new ArrayList<>(searchResult.getStopTimes());
        List<Long> thisStopTimes = new ArrayList<>(thisSearchResult.getStopTimes());

        //pokud jsem na through stanici pouze projel jednim spojem tak ji nechci ve vysledcich
        if(stopTimes.get(stopTimes.size() - 1).equals(thisStopTimes.get(0))) {
            stopTimes.remove(stopTimes.size() - 1);
            thisStopTimes.remove(0);

            //a soucasne ubiram jeden prestup, protoze jsem ve skutecnosti neprestoupil ale zustal ve stejnem voze
            ret.setNumberOfTransfers(ret.getNumberOfTransfers() - 1);
        }

        stopTimes.addAll(thisStopTimes);
        ret.setStopTimes(stopTimes);

        return ret;
    }

    /**
     * @param searchResult vysledek vyhledavani
     * @param departure caj vyjezdu od
     * @return spravny datum dle departure ale zvyseny o 1 den, pokud byl prijezd po pulnoci
     */
    private DateTime getThisDateTime(SearchResult searchResult, DateTime departure) {
        return searchResult.isOverMidnightArrival() ? new DateTime(departure).plusDays(1) : new DateTime(departure);
    }

    /**
     * @param searchResult vysledek vyhledavani
     * @param departure caj vyjezdu od
     * @return spravny datum ziskany ze searchResult.getArrival a naroubovany na den departure (tedy vteriny arrival se nastavi do dne departure)
     */
    private DateTime getThisArrival(SearchResult searchResult, DateTime departure) {
        final DateTime thisDeparture = getThisDateTime(searchResult, departure);
        return thisDeparture.withMillisOfDay((int) searchResult.getArrival() * 1000);
    }

    /**
     * @param searchResult vysledek vyhledavani
     * @param departure caj vyjezdu od
     * @return spravny datum ziskany ze searchResult.getDeparture a naroubovany na den departure (tedy vteriny departure se nastavi do dne departure)
     */
    private DateTime getThisDeparture(SearchResult searchResult, DateTime departure) {
        final DateTime thisDeparture = getThisDateTime(searchResult, departure);
        return thisDeparture.withMillisOfDay((int) searchResult.getDeparture() * 1000);
    }

    /**
     * @param searchResult vysledek vyhledavani
     * @param arrival caj prijezdu do
     * @return spravny datum dle arrival ale snizeny o 1 den, pokud byl prijezd pred pulnoci
     */
    private DateTime getThisDateTimeArrival(SearchResult searchResult, DateTime arrival) {
        return searchResult.isOverMidnightDeparture() ? new DateTime(arrival).minusDays(1) : new DateTime(arrival);
    }

    /**
     * @param searchResult vysledek vyhledavani
     * @param arrival caj prijezdu do
     * @return spravny datum ziskany ze searchResult.getArrival a naroubovany na den arrival (tedy vteriny arrival se nastavi do dne arrival)
     */
    private DateTime getThisArrivalForArrival(SearchResult searchResult, DateTime arrival) {
        final DateTime thisArrival = getThisDateTimeArrival(searchResult, arrival);
        return thisArrival.withMillisOfDay((int) searchResult.getArrival() * 1000);
    }

    /**
     * @param searchResult vysledek vyhledavani
     * @param arrival caj prijezdu do
     * @return spravny datum ziskany ze searchResult.getDeparture a naroubovany na den arrival (tedy vteriny departure se nastavi do dne arrival)
     */
    private DateTime getThisDepartureForArrival(SearchResult searchResult, DateTime arrival) {
        final DateTime thisArrival = getThisDateTimeArrival(searchResult, arrival);
        return thisArrival.withMillisOfDay((int) searchResult.getDeparture() * 1000);
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

    /**
     * naplni mapu parametry
     * @param params mapa parametru
     * @param stopTimeId id stopTime
     * @param arrival prijezd
     * @param maxTransfers max pocet prestupu
     */
    private void fillArrivalParams(Map<String, Object> params, Long stopTimeId, long arrival, int maxTransfers) {
        params.put("stopTimeId", stopTimeId);
        params.put("arrival", arrival);
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
