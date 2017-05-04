package cz.cvut.dp.nss.batch.input.stopTime;

import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import cz.cvut.dp.nss.services.trip.Trip;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.Properties;

/**
 * Process importu zastaveni.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "stopTimeBatchProcessor")
public class StopTimeBatchProcessor implements ItemProcessor<DefaultFieldSet, StopTime> {

    @Override
    public StopTime process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        StopTime stopTime = new StopTime();

        String arrivalTime = fix24DateTime((String) properties.get("arrival_time"));
        String departureTime = fix24DateTime((String) properties.get("departure_time"));
        if(arrivalTime != null) {
            stopTime.setArrival(LocalTime.parse(arrivalTime, DateTimeUtils.GTFS_TIME_PATTERN_DATE_TIME_FORMATTER));
        }
        if(departureTime != null) {
            stopTime.setDeparture(LocalTime.parse(departureTime, DateTimeUtils.GTFS_TIME_PATTERN_DATE_TIME_FORMATTER));
        }
        stopTime.setSequence(Integer.parseInt((String) properties.get("stop_sequence")));

        Trip trip = new Trip();
        trip.setId((String) properties.get("trip_id"));
        stopTime.setTrip(trip);

        Stop stop = new Stop();
        stop.setId((String) properties.get("stop_id"));
        stopTime.setStop(stop);

        return stopTime;
    }

    /**
     * pokud cas zacina 24 a vice tak ho prevede na modulo 24
     * @param dateTime string dateTime
     * @return opraveny date time z rozsahu H: 00 - 23
     */
    private static String fix24DateTime(String dateTime) {
        if(!StringUtils.hasText(dateTime)) return null;

        String[] split = dateTime.split(":");
        int hours = Integer.parseInt(split[0]);
        if(hours >= 24) {
            hours = hours % 24;
            String hoursString = hours < 10 ? "0" + hours : hours + "";
            dateTime = hoursString + dateTime.substring(2, dateTime.length());
        }

        return dateTime;
    }

}
