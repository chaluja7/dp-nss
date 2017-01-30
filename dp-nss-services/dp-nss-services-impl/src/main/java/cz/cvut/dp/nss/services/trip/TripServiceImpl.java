package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.persistence.trip.TripDao;
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
 * Implementation of TripService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class TripServiceImpl extends AbstractEntityService<Trip, String, TripDao> implements TripService {

    @Autowired
    public TripServiceImpl(TripDao dao) {
        super(dao);
    }

    //TODO - opravdu by to neslo resit jinak? - problem je jak volat metody z iteratoru a otevrit u toho transakci
    @Resource(name = "tripServiceImpl")
    private TripService tripService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Trip> getAllForInsertToGraph(final int start, final int limit) {
        if(limit > 1000) throw new RuntimeException("too many results to return");
        return dao.getAllForInsertToGraph(start, limit);
    }

    @Override
    public Iterator<Trip> iteratorOverTripsForInsertToGraph() {
        return new BatchProcessingTripsIterator();
    }

    private class BatchProcessingTripsIterator extends BatchProcessingIterator<Trip> {

        BatchProcessingTripsIterator() {
            super(tripService.getAllForInsertToGraph(0, MAX_NUMBER_OF_RESULTS_PER_QUERY));
        }

        @Override
        public Trip next() {
            if(!entityIterator.hasNext()) throw new NoSuchElementException();

            Trip trip = entityIterator.next();
            //Pokud jiz neni zadny dalsi prvek (pro pristi volani next()) nactu nova data, pokud jeste existuji
            if(!entityIterator.hasNext()) {
                if(mayHaveNextChunk()) {
                    loadNextChunk();
                }
            }

            return trip;
        }

        @Override
        protected void loadNextChunk() {
            setNextChunk(tripService.getAllForInsertToGraph(getStart(), getLimit()));
        }

    }

}
