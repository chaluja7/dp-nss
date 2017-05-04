package cz.cvut.dp.nss.batch.input.trip;

import cz.cvut.dp.nss.batch.BatchStringUtils;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripWheelchairAccessibleType;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Processor importu jizdy.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "tripBatchProcessor")
public class TripBatchProcessor implements ItemProcessor<DefaultFieldSet, Trip> {

    @Override
    public Trip process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        Trip trip = new Trip();
        trip.setId((String) properties.get("trip_id"));
        trip.setShapeId(BatchStringUtils.notEmptyStringOrNull((String) properties.get("shape_id")));
        trip.setHeadSign(BatchStringUtils.notEmptyStringOrNull((String) properties.get("trip_headsign")));

        String wheelchairString = (String) properties.get("wheelchair_accessible");
        if(StringUtils.hasText(wheelchairString)) {
            trip.setTripWheelchairAccessibleType(EnumUtils.fromCode(Integer.parseInt(wheelchairString), TripWheelchairAccessibleType.class));
        } else {
            trip.setTripWheelchairAccessibleType(TripWheelchairAccessibleType.NO_INFO);
        }

        //obe jsou required
        Calendar calendar = new Calendar();
        calendar.setId((String) properties.get("service_id"));
        trip.setCalendar(calendar);

        Route route = new Route();
        route.setId((String) properties.get("route_id"));
        trip.setRoute(route);

        return trip;
    }

}
