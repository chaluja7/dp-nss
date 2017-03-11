package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.persistence.stop.StopDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.common.BatchProcessingIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Implementation of StopService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class StopServiceImpl extends AbstractEntityService<Stop, String, StopDao> implements StopService {

    @Autowired
    public StopServiceImpl(StopDao dao) {
        super(dao);
    }

    @Resource(name = "stopServiceImpl")
    private StopService stopService;

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Stop> getAllInRange(int start, int limit) {
        return dao.getAllInRange(start, limit);
    }

    @Override
    public Iterator<Stop> iteratorOverAllStops() {
        return new BatchProcessingStopsIterator();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public Set<String> findStopNamesByStartPattern(String startPattern) {
        if(startPattern == null || startPattern.length() < 3) return new HashSet<>();

        //vytahnu stanice z db
        List<String> stops = dao.findStopNamesByStartPattern(startPattern);

        //vytvorim si set pro navraceni vysledku (zachova razeni z dao vrstvy)
        Set<String> set = new LinkedHashSet<>();
        for(String stop : stops) {
            //a do setu vkladam fixle nazvy stanic (a samozrejme unikatne)
            set.add(getFixedStopName(stop));
        }

        return set;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Stop> findStopsBySearchQuery(String searchQuery) {
        if(searchQuery == null || searchQuery.length() < 3) return new ArrayList<>();
        return dao.findStopsBySearchQuery(searchQuery);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Stop> getByFilter(final StopFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc) {
        if(limit != null && limit <= 0) return new ArrayList<>();
        return dao.getByFilter(filter, getOffsetOrDefault(offset), getLimitOrDefault(limit), orderColumn != null ? orderColumn : "", asc);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public long getCountByFilter(final StopFilter filter) {
        return dao.getCountByFilter(filter);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean canBeDeleted(String id) {
        Stop stop = get(id);
        if(stop != null) {
            return !(stop.getChildStops().size() > 0 || stop.getStopTimes().size() > 0);
        }

        return false;
    }

    private class BatchProcessingStopsIterator extends BatchProcessingIterator<Stop> {

        BatchProcessingStopsIterator() {
            super(stopService.getAllInRange(0, MAX_NUMBER_OF_RESULTS_PER_QUERY));
        }

        @Override
        public Stop next() {
            if(!entityIterator.hasNext()) throw new NoSuchElementException();

            Stop stop = entityIterator.next();
            //Pokud jiz neni zadny dalsi prvek (pro pristi volani next()) nactu nova data, pokud jeste existuji
            if(!entityIterator.hasNext()) {
                if(mayHaveNextChunk()) {
                    loadNextChunk();
                }
            }

            return stop;
        }

        @Override
        protected void loadNextChunk() {
            setNextChunk(stopService.getAllInRange(getStart(), getLimit()));
        }

    }

    /**
     * @param stopName jmeno stanice
     * @return jmeno stanice upraveno pro neo4j (napr. z "Mustek - A" udela jen "Mustek") - kvuli moznosti prestupu
     */
    public static String getFixedStopName(String stopName) {
        if(stopName == null) return null;

        if(stopName.endsWith(" - A") || stopName.endsWith(" - B") || stopName.endsWith(" - C")) {
            stopName = stopName.substring(0, stopName.length() - 4);
        }

        //TODO - jmeno stanice muze koncit take vyrazem v zavorce (např. 'Želivského (ul. Votická)')
        //TODO - v tom případě je nutné tu závorku smazat a nechat jen 'Želivského'

        return stopName;
    }

}
