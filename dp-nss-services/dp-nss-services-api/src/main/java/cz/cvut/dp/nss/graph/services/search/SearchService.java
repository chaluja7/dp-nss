package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
public interface SearchService {

    int DEFAULT_MAX_HOUR_AFTER_DEPARTURE = 3;

    int DEFAULT_MAX_NUMBER_OF_RESULTS = 3;

    /**
     * Najde spojeni dle data odjezdu
     * @param stopFromName stanice z
     * @param stopToName stanice do
     * @param stopThroughName prujezdni/prestupni stanice
     * @param departure datum odjezdu
     * @param maxDeparture maximalni cas prijezdu
     * @param maxTransfers max pocet prestupu
     * @param maxNumberOfResults max pocet vracenych vysledku
     * @param withWheelChair pokud true hledame jen bezbarierove tripy a stopy
     * @param alreadyFoundedSearchResultsWithThroughStop pokud neni null tak obsahuje nalezene vysledky z drivejsi iterace. zavolano s ne null hodnotou slouzi k doplneni dalsich vysledku (s pozdejsim departure)
     * @return list vysledku
     */
    List<SearchResult> findPathByDepartureDate(String stopFromName, String stopToName, String stopThroughName,
                                               DateTime departure, DateTime maxDeparture, int maxTransfers, int maxNumberOfResults,
                                               boolean withWheelChair, List<SearchResult> alreadyFoundedSearchResultsWithThroughStop);

}
