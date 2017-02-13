package cz.cvut.dp.nss.batch.graph.calendar;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNode;
import cz.cvut.dp.nss.graph.services.calendar.CalendarNodeService;
import org.neo4j.ogm.session.Session;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphCalendarBatchWriter")
public class GraphCalendarBatchWriter implements ItemWriter<CalendarNode> {

    @Autowired
    protected CalendarNodeService calendarNodeService;

    @Autowired
    protected Session session;

    @Override
    @Transactional("neo4jTransactionManager")
    public void write(List<? extends CalendarNode> items) throws Exception {
        //velmi dulezite, jinak postupne dojde k degradaci rychlosti zapisovani
        session.clear();

        for(CalendarNode calendarNode : items) {
            //ulozi se jak calendar tak navazene (a uz propojene) calendarDates
            calendarNodeService.save(calendarNode, -1);
        }
    }

}
