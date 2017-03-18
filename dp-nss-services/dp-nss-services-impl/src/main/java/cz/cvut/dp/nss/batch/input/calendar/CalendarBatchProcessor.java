package cz.cvut.dp.nss.batch.input.calendar;

import cz.cvut.dp.nss.batch.BatchStringUtils;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Properties;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "calendarBatchProcessor")
public class CalendarBatchProcessor implements ItemProcessor<DefaultFieldSet, Calendar> {

    @Override
    public Calendar process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        Calendar calendar = new Calendar();
        calendar.setId((String) properties.get("service_id"));
        calendar.setMonday(BatchStringUtils.booleanValue((String) properties.get("monday")));
        calendar.setTuesday(BatchStringUtils.booleanValue((String) properties.get("tuesday")));
        calendar.setWednesday(BatchStringUtils.booleanValue((String) properties.get("wednesday")));
        calendar.setThursday(BatchStringUtils.booleanValue((String) properties.get("thursday")));
        calendar.setFriday(BatchStringUtils.booleanValue((String) properties.get("friday")));
        calendar.setSaturday(BatchStringUtils.booleanValue((String) properties.get("saturday")));
        calendar.setSunday(BatchStringUtils.booleanValue((String) properties.get("sunday")));

        calendar.setStartDate(LocalDate.parse((String) properties.get("start_date"), DateTimeUtils.GTFS_DATE_PATTERN_DATE_TIME_FORMATTER));
        calendar.setEndDate(LocalDate.parse((String) properties.get("end_date"), DateTimeUtils.GTFS_DATE_PATTERN_DATE_TIME_FORMATTER));

        return calendar;
    }

}
