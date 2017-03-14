package cz.cvut.dp.nss.graph.repository.calendarDate;

import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
public interface CalendarDateNodeRepository extends GraphRepository<CalendarDateNode> {

    /**
     * najde calendarDate dle calendarDateId
     * @param calendarDateId calendarDateId
     * @return calendarDateNode dle calendarDateId
     */
    @Query("match (n:CalendarDateNode {calendarDateId: {0}}) return n")
    CalendarDateNode findByCalendarDateId(Long calendarDateId);

    /**
     * smaze vsechny calendarDates navazana na calendarId
     * @param calendarId calendarId
     */
    @Query("match (n:CalendarNode {calendarId: {0}})<-[IN_CALENDAR]-(m) detach delete m")
    void deleteByCalendarId(String calendarId);

}
