package cz.cvut.dp.nss.graph.services.search.wrappers;

import java.util.Comparator;

/**
 * Slouzi pro serazeni vysledku vyhledavani spojeni.
 *
 * @author jakubchalupa
 * @since 12.03.15
 */
public class SearchResultByDepartureDateComparator implements Comparator<SearchResult> {

    @Override
    public int compare(SearchResult o1, SearchResult o2) {

        if(!o1.isOverMidnightArrival() && o2.isOverMidnightArrival()) {
            return -1;
        }

        if(o1.isOverMidnightArrival() && !o2.isOverMidnightArrival()) {
            return 1;
        }

        if(o1.getArrival() < o2.getArrival()) {
            return -1;
        }

        if(o1.getArrival() > o2.getArrival()) {
            return 1;
        }

        if(o1.getDeparture() > o2.getDeparture()) {
            return -1;
        }

        if(o1.getDeparture() < o2.getDeparture()) {
            return 1;
        }

        if(o1.getNumberOfTransfers() < o2.getNumberOfTransfers()) {
            return -1;
        }

        if(o1.getNumberOfTransfers() > o2.getNumberOfTransfers()) {
            return 1;
        }

        return 0;
    }

}
