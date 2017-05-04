package cz.cvut.dp.nss.batch.output.stopTime;

import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * RowMapper pro export zastaveni.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class StopTimeBatchRowMapper implements RowMapper<StopTimeBatchWrapper> {

    @Override
    public StopTimeBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        StopTimeBatchWrapper stopTime = new StopTimeBatchWrapper();
        stopTime.setId(rs.getBigDecimal("id"));
        stopTime.setTripId(rs.getString("trip_id"));
        stopTime.setStopId(rs.getString("stop_id"));
        stopTime.setSequence(rs.getInt("sequence"));

        DateFormat df = new SimpleDateFormat(DateTimeUtils.GTFS_TIME_PATTERN);
        Time arrival = rs.getTime("arrival");
        if(arrival != null) {
            stopTime.setArrival(df.format(arrival));
        }
        Time departure = rs.getTime("departure");
        if(departure != null) {
            stopTime.setDeparture(df.format(departure));
        }

        return stopTime;
    }

}
