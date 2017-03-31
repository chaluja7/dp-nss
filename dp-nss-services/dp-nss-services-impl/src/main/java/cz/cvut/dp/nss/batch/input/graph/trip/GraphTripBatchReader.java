package cz.cvut.dp.nss.batch.input.graph.trip;

import cz.cvut.dp.nss.services.trip.TripService;
import cz.cvut.dp.nss.services.trip.TripWrapper;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Iterator;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphTripBatchReader")
@Scope("step")
public class GraphTripBatchReader extends AbstractItemCountingItemStreamItemReader<TripWrapper> {

    @Autowired
    protected TripService tripService;

    private Iterator<TripWrapper> trips;

    private int offset = 0;

    private static final int MAX_READ_BATCH_SIZE = 10;

    public GraphTripBatchReader() {
        this.setName(ClassUtils.getShortName(GraphTripBatchReader.class));
    }

    @Override
    protected TripWrapper doRead() throws Exception {
        if(trips.hasNext()) {
            return trips.next();
        }

        //zkusime nacist dalsi davku z db
        trips = tripService.getAllForInsertToGraph(MAX_READ_BATCH_SIZE, ++offset * MAX_READ_BATCH_SIZE).iterator();
        if(trips.hasNext()) {
            return trips.next();
        }

        //uz opravdu nic nemame
        return null;
    }

    @Override
    protected void doOpen() throws Exception {
        trips = tripService.getAllForInsertToGraph(MAX_READ_BATCH_SIZE, offset * MAX_READ_BATCH_SIZE).iterator();
    }

    @Override
    protected void doClose() throws Exception {
        //empty
    }

}
