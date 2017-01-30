package cz.cvut.dp.nss.batch.graph.trip;

import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripService;
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
public class GraphTripBatchReader extends AbstractItemCountingItemStreamItemReader<Trip> {

    @Autowired
    protected TripService tripService;

    private Iterator<Trip> trips;

    public GraphTripBatchReader() {
        this.setName(ClassUtils.getShortName(GraphTripBatchReader.class));
    }

    @Override
    protected Trip doRead() throws Exception {
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
