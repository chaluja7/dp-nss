package cz.cvut.dp.nss.batch.graph.calendar;

import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Iterator;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphCalendarBatchReader")
@Scope("step")
public class GraphCalendarBatchReader extends AbstractItemCountingItemStreamItemReader<Calendar> {

    @Autowired
    protected CalendarService calendarService;

    private Iterator<Calendar> calendars;

    public GraphCalendarBatchReader() {
        this.setName(ClassUtils.getShortName(GraphCalendarBatchReader.class));
    }

    @Override
    protected Calendar doRead() throws Exception {
        if(calendars.hasNext()) {
            return calendars.next();
        }

        return null;
    }

    @Override
    protected void doOpen() throws Exception {
        calendars = calendarService.getAllForInsertToGraph().iterator();
    }

    @Override
    protected void doClose() throws Exception {
        //empty
    }

}
