package cz.cvut.dp.nss.persistence.trip;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.services.stop.StopWheelchairBoardingType;
import cz.cvut.dp.nss.services.stopTime.StopTimeWrapper;
import cz.cvut.dp.nss.services.trip.TripWheelchairAccessibleType;
import cz.cvut.dp.nss.services.trip.TripWrapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.Timestamp;
import java.util.*;

/**
 * Spring JDBC implementace repository jizdy.
 *
 * @author jakubchalupa
 * @since 12.02.17
 */
public class JdbcTripDao extends JdbcDaoSupport {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * @param limit max pocet tripu
     * @param offset offset (razeno dle id tripu asc)
     * @return tripy pro vlozeni do grafu
     */
    public List<TripWrapper> getAllForInsertToGraph(int limit, int offset) {
        final String schema = SchemaThreadLocal.getOrDefault();
        final String tripsTable = schema + ".trips";
        final String stopTimesTable = schema + ".stop_times";
        final String stopsTable = schema + ".stops";

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select t.id as t_id, t.calendar_id as t_calendar_id, t.wheelChair as t_wheelChair, st.id as st_id, st.sequence as st_sequence, ");
        sqlBuilder.append("st.arrival as st_arrival, st.departure as st_departure, s.id as s_id, s.name as s_name, s.wheelChair as s_wheelChair");
        sqlBuilder.append(" from ").append(tripsTable).append(" t left outer join ").append(stopTimesTable);
        sqlBuilder.append(" st on t.id = st.trip_id left outer join ").append(stopsTable);
        sqlBuilder.append(" s on st.stop_id = s.id where t.id in (select id from ").append(tripsTable).append(" order by id limit :limit offset :offset)");
        sqlBuilder.append(" order by t.id, st.sequence");

        final Map<String, TripWrapper> trips = new LinkedHashMap<>();
        RowMapper<StopTimeWrapper> tripWrapperRowMapper = (rs, rowNum) -> {
            String tripId = rs.getString("t_id");
            TripWrapper tripWrapper = trips.get(tripId);
            if(tripWrapper == null) {
                tripWrapper = new TripWrapper();
                tripWrapper.setId(tripId);
                tripWrapper.setCalendarId(rs.getString("t_calendar_id"));
                final String tripWheelChair = rs.getString("t_wheelChair");
                tripWrapper.setWheelChair(TripWheelchairAccessibleType.ACCESSIBLE.name().equals(tripWheelChair));
                trips.put(tripId, tripWrapper);
            }

            StopTimeWrapper stopTimeWrapper = new StopTimeWrapper();
            stopTimeWrapper.setId(rs.getLong("st_id"));
            stopTimeWrapper.setSequence(rs.getInt("st_sequence"));

            Timestamp arrival = rs.getTimestamp("st_arrival");
            if(arrival != null) stopTimeWrapper.setArrival(arrival.toLocalDateTime().toLocalTime());

            Timestamp departure = rs.getTimestamp("st_departure");
            if(departure != null) stopTimeWrapper.setDeparture(departure.toLocalDateTime().toLocalTime());

            stopTimeWrapper.setStopId(rs.getString("s_id"));
            stopTimeWrapper.setStopName(rs.getString("s_name"));
            final String stopWheelChair = rs.getString("s_wheelChair");
            stopTimeWrapper.setStopWheelChair(StopWheelchairBoardingType.BOARDING_POSSIBLE.name().equals(stopWheelChair));

            tripWrapper.getStopTimeWrappers().add(stopTimeWrapper);
            return stopTimeWrapper;
        };

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("offset", offset);

        namedParameterJdbcTemplate.query(sqlBuilder.toString(), params, tripWrapperRowMapper);
        return new ArrayList<>(trips.values());
    }

}
