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

    public GraphTripBatchReader() {
        this.setName(ClassUtils.getShortName(GraphTripBatchReader.class));
    }

    @Override
    protected TripWrapper doRead() throws Exception {
        if(trips.hasNext()) {
            return trips.next();
        }

        return null;
    }

    @Override
    protected void doOpen() throws Exception {
        trips = tripService.getAllForInsertToGraph().iterator();
    }

    @Override
    protected void doClose() throws Exception {
        //empty
    }

}
