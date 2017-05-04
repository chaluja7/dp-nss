package cz.cvut.dp.nss.batch.output.trip;

import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.trip.TripWheelchairAccessibleType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper pro export jizdy.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class TripBatchRowMapper implements RowMapper<TripBatchWrapper> {

    @Override
    public TripBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        TripBatchWrapper trip = new TripBatchWrapper();
        trip.setId(rs.getString("id"));
        trip.setHeadSign(rs.getString("headSign"));
        trip.setShapeId(rs.getString("shape_id"));
        trip.setRouteId(rs.getString("route_id"));
        trip.setCalendarId(rs.getString("calendar_id"));

        String wheelChairType = rs.getString("wheelChair");
        if(!StringUtils.isEmpty(wheelChairType)) {
            trip.setWheelChairType(EnumUtils.fromName(wheelChairType, TripWheelchairAccessibleType.class).getCode());
        }

        return trip;
    }

}
