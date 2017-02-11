package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResultWrapper;

import java.time.LocalDateTime;
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
     * @param maxResults max pocet vracenych vysledku
     * @return list vysledku
     */
    List<SearchResultWrapper> findPathByDepartureDate(String stopFromName, String stopToName, LocalDateTime departure, int maxHoursAfterDeparture, int maxTransfers, int maxResults);

}
