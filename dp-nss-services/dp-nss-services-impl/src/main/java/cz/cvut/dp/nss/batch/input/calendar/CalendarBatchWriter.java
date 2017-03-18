package cz.cvut.dp.nss.batch.input.calendar;

import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "calendarBatchWriter")
public class CalendarBatchWriter implements ItemWriter<Calendar> {

    @Autowired
    protected CalendarService calendarService;

    @Override
    public void write(List<? extends Calendar> items) throws Exception {
        for(Calendar calendar : items) {
            calendarService.create(calendar);
        }
    }

}
