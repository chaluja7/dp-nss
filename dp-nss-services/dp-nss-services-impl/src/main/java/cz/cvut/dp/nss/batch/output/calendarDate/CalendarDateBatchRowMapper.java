package cz.cvut.dp.nss.batch.output.calendarDate;

import cz.cvut.dp.nss.services.calendarDate.CalendarExceptionType;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.common.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class CalendarDateBatchRowMapper implements RowMapper<CalendarDateBatchWrapper> {

    @Override
    public CalendarDateBatchWrapper mapRow(ResultSet rs, int i) throws SQLException {
        CalendarDateBatchWrapper calendarDate = new CalendarDateBatchWrapper();
        calendarDate.setId(rs.getBigDecimal("id"));
        calendarDate.setCalendarId(rs.getString("calendar_id"));

        DateFormat df = new SimpleDateFormat(DateTimeUtils.GTFS_DATE_PATTERN);
        Date date = rs.getDate("date");
        if(date != null) {
            calendarDate.setDate(df.format(date));
        }

        String exceptionType = rs.getString("exceptionType");
        if(!StringUtils.isEmpty(exceptionType)) {
            calendarDate.setExceptionType(EnumUtils.fromName(exceptionType, CalendarExceptionType.class).getCode());
        }

        return calendarDate;
    }

}
