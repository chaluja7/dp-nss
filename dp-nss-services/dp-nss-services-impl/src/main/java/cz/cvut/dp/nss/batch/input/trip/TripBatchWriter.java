package cz.cvut.dp.nss.batch.input.trip;

import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "tripBatchWriter")
public class TripBatchWriter implements ItemWriter<Trip> {

    @Autowired
    protected TripService tripService;

    @Override
    public void write(List<? extends Trip> items) throws Exception {
        for(Trip trip : items) {
            tripService.create(trip);
        }
    }

}
