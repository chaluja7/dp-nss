package cz.cvut.dp.nss.batch.calendarDate;

import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import cz.cvut.dp.nss.services.calendarDate.CalendarDateService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "calendarDateBatchWriter")
public class CalendarDateBatchWriter implements ItemWriter<CalendarDate> {

    @Autowired
    protected CalendarDateService calendarDateService;

    @Override
    public void write(List<? extends CalendarDate> items) throws Exception {
        for(CalendarDate calendarDate : items) {
            calendarDateService.create(calendarDate);
        }
    }

}
