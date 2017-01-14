package cz.cvut.dp.nss.batch.calendarDate;

import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import cz.cvut.dp.nss.services.calendarDate.CalendarExceptionType;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.common.EnumUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Properties;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "calendarDateBatchProcessor")
public class CalendarDateBatchProcessor implements ItemProcessor<DefaultFieldSet, CalendarDate> {

    @Override
    public CalendarDate process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setDate(LocalDate.parse((String) properties.get("date"), DateTimeUtils.GTFS_DATE_PATTERN_DATE_TIME_FORMATTER));
        calendarDate.setExceptionType(EnumUtils.fromCode(Integer.parseInt((String) properties.get("exception_type")), CalendarExceptionType.class));

        //a musim priradit virtualni (jiz v db existujici!) calendar s id
        Calendar calendar = new Calendar();
        calendar.setId((String) properties.get("service_id"));
        calendarDate.setCalendar(calendar);

        return calendarDate;
    }

}
