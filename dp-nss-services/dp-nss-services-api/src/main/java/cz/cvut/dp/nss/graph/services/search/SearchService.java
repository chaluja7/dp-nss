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

    /**
     * Najde spojeni dle data odjezdu
     * @param stopFromName stanice z
     * @param stopToName stanice do
     * @param departure datum odjezdu
     * @param maxHoursAfterDeparture max pocet hodin, o ktere muze spoj vyjet pozdeji nez departure
     * @param maxTransfers max pocet prestupu
     * @return list vysledku
     */
    List<SearchResult> findPathByDepartureDate(String stopFromName, String stopToName, DateTime departure, int maxHoursAfterDeparture, int maxTransfers);

}
