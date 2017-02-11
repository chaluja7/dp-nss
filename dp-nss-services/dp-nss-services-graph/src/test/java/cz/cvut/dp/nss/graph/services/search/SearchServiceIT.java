package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.AbstractServiceIT;
import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResultWrapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
public class SearchServiceIT extends AbstractServiceIT {

    @Autowired
    private SearchService searchService;

    @Test
    public void testSearch() {
        List<SearchResultWrapper> pathByDepartureDate = searchService.findPathByDepartureDate("from", "to", LocalDateTime.now(), 1, 1, 3);
        int i = 0;
    }


}
