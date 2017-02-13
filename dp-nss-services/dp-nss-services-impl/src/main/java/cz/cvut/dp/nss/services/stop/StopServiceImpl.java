package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.persistence.stop.StopDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.common.BatchProcessingIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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

    //TODO - opravdu by to neslo resit jinak? - problem je jak volat metody z iteratoru a otevrit u toho transakci
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

}
