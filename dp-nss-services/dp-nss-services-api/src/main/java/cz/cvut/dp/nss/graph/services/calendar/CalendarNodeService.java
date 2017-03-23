package cz.cvut.dp.nss.graph.services.calendar;

import cz.cvut.dp.nss.graph.services.common.NodeService;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
public interface CalendarNodeService extends NodeService<CalendarNode> {

    /**
     * najde calendar dle calendarId
     * @param calendarId calendarId
     * @return calendarNode dle calendarId
     */
    CalendarNode findByCalendarId(String calendarId);

    /**
     * zavola initCalendarDates na neo4j
     */
    void initCalendarDates();

}
