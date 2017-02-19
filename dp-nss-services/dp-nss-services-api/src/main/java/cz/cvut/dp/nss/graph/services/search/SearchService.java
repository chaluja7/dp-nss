package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResultWrapper;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
public interface SearchService {

    /**
     * Najde spojeni dle data odjezdu
     * @param stopFromName stanice z
     * @param stopToName stanice do
     * @param departure datum odjezdu
     * @param maxHoursAfterDeparture max pocet hodin, o ktere muze spoj vyjet pozdeji nez departure
     * @param maxTransfers max pocet prestupu
     * @return list vysledku
     */
    List<SearchResultWrapper> findPathByDepartureDate(String stopFromName, String stopToName, DateTime departure, int maxHoursAfterDeparture, int maxTransfers);

    /**
     * zavola initCalendarDates na neo4j
     */
    void initCalendarDates();

}
