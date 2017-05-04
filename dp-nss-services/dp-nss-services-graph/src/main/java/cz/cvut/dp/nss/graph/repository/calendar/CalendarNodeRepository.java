package cz.cvut.dp.nss.graph.repository.calendar;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Repository intervalu platnosti.
 *
 * @author jakubchalupa
 * @since 11.02.17
 */
public interface CalendarNodeRepository extends GraphRepository<CalendarNode> {

    /**
     * najde calendar dle calendarId
     * @param calendarId calendarId
     * @return calendarNode dle calendarId
     */
    @Query("match (n:CalendarNode {calendarId: {0}}) return n")
    CalendarNode findByCalendarId(String calendarId);

}
