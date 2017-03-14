package cz.cvut.dp.nss.graph.services.calendarDate;

import cz.cvut.dp.nss.graph.services.common.NodeService;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
public interface CalendarDateNodeService extends NodeService<CalendarDateNode> {

    /**
     * najde calendarDate dle calendarDateId
     * @param calendarDateId calendarDateId
     * @return calendarDateNode dle calendarDateId
     */
    CalendarDateNode findByCalendarDateId(Long calendarDateId);

    /**
     * smaze vsechny calendarDates navazana na calendarId
     * @param calendarId calendarId
     */
    void deleteByCalendarId(String calendarId);

}
