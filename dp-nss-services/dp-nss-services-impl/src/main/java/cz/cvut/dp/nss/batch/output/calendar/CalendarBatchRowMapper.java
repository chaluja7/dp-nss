package cz.cvut.dp.nss.batch.output.calendar;

import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * RowMapper pro export intervalu platnosti.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class CalendarBatchRowMapper implements RowMapper<CalendarBatchWrapper> {

    @Override
    public CalendarBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        CalendarBatchWrapper calendar = new CalendarBatchWrapper();
        calendar.setId(rs.getString("id"));
        calendar.setMonday(rs.getBoolean("monday"));
        calendar.setTuesday(rs.getBoolean("tuesday"));
        calendar.setWednesday(rs.getBoolean("wednesday"));
        calendar.setThursday(rs.getBoolean("thursday"));
        calendar.setFriday(rs.getBoolean("friday"));
        calendar.setSaturday(rs.getBoolean("saturday"));
        calendar.setSunday(rs.getBoolean("sunday"));

        DateFormat df = new SimpleDateFormat(DateTimeUtils.GTFS_DATE_PATTERN);
        Date startDate = rs.getDate("startDate");
        if(startDate != null) {
            calendar.setStartDate(df.format(startDate));
        }
        Date endDate = rs.getDate("endDate");
        if(startDate != null) {
            calendar.setEndDate(df.format(endDate));
        }

        return calendar;
    }

}
