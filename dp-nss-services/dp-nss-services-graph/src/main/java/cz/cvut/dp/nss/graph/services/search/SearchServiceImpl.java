package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResultWrapper;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    protected Session session;

    @Override
    public List<SearchResultWrapper> findPathByDepartureDate(String stopFromName, String stopToName, LocalDateTime departure, int maxHoursAfterDeparture, int maxTransfers, int maxResults) {

        //TODO pouze docasne, bude treba volat se spravnymi parametry a zpracovat vracene vysledky
        Result query = session.query("CALL cz.cvut.dp.nss.search.byDepartureSearch('BrownA_T01')", new HashMap<>(), true);

        return null;
    }

}
