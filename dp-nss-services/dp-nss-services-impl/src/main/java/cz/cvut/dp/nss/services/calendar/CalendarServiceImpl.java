package cz.cvut.dp.nss.services.calendar;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNode;
import cz.cvut.dp.nss.graph.services.calendar.CalendarNodeService;
import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNode;
import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNodeService;
import cz.cvut.dp.nss.persistence.calendar.CalendarDao;
import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import cz.cvut.dp.nss.services.calendarDate.CalendarDateService;
import cz.cvut.dp.nss.services.calendarDate.CalendarExceptionType;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CalendarService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class CalendarServiceImpl extends AbstractEntityService<Calendar, String, CalendarDao> implements CalendarService {

    private TripService tripService;

    private CalendarDateService calendarDateService;

    private CalendarNodeService calendarNodeService;

    private CalendarDateNodeService calendarDateNodeService;

    @Autowired
    public CalendarServiceImpl(CalendarDao dao, CalendarDateService calendarDateService, TripService tripService,
                               CalendarNodeService calendarNodeService, CalendarDateNodeService calendarDateNodeService) {
        super(dao);
        this.calendarDateService = calendarDateService;
        this.tripService = tripService;
        this.calendarNodeService = calendarNodeService;
        this.calendarDateNodeService = calendarDateNodeService;
    }

    @Override
    @Transactional(value = "transactionManager")
    public void update(Calendar calendar) {
        update(calendar, true);
    }

    @Override
    @Transactional(value = "transactionManager")
    public void create(Calendar calendar) {
        create(calendar, true);
    }

    @Override
    @Transactional(value = "transactionManager")
    public void delete(String s) {
        delete(s, true);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Calendar> getAllForInsertToGraph() {
        return dao.getAllForInsertToGraph();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Calendar> getAllOrdered() {
        return dao.getAllOrdered();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Calendar> getByFilter(final CalendarFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc) {
        if(limit != null && limit <= 0) return new ArrayList<>();
        return dao.getByFilter(filter, getOffsetOrDefault(offset), getLimitOrDefault(limit), orderColumn != null ? orderColumn : "", asc);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public long getCountByFilter(final CalendarFilter filter) {
        return dao.getCountByFilter(filter);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean canBeDeleted(String id) {
        return tripService.getCountByCalendarId(id) == 0;
    }

    @Override
    @Transactional(value = "transactionManager")
    public void create(Calendar calendar, boolean neo4jSynchronize) {
        dao.create(calendar);

        if(neo4jSynchronize) {
            //TODO aktualizovat mapu ve vyhledavaci neo4j pres cypher!
            //soucasne musim data ulozit do neo4j!
            //vytvorim si objekt pro neo4j
            CalendarNode calendarNode = getCalendarNodeFromCalendar(calendar);
            //a ulozim vsechno naraz
            calendarNodeService.save(calendarNode, -1);
        }
    }

    @Override
    @Transactional(value = "transactionManager")
    public void update(Calendar calendar, boolean neo4jSynchronize) {
        //nejdriv smazu vsechny calendarDate k tomuto zaznamu z db
        Calendar currentInDb = get(calendar.getId());
        if(currentInDb.getCalendarDates() != null) {
            for(CalendarDate calendarDate : currentInDb.getCalendarDates()) {
                calendarDateService.delete(calendarDate.getId());
            }
        }

        //pak provedu update calendar zaznamu
        dao.update(calendar);

        //a nakonec ulozim vsechny calendarDates znovu
        if(calendar.getCalendarDates() != null) {
            for(CalendarDate calendarDate : calendar.getCalendarDates()) {
                calendarDate.setId(null);
                calendarDateService.create(calendarDate);
            }
        }

        if(neo4jSynchronize) {
            //TODO aktualizovat mapu ve vyhledavaci neo4j pres cypher!
            //update take v neo4j!
            //smazu vsechny calendarDateNodes k tomuto calendar z neo4j
            calendarDateNodeService.deleteByCalendarId(calendar.getId());

            //vytvorim si novy objekt calendarNode
            CalendarNode calendarNode = getCalendarNodeFromCalendar(calendar);
            //nasetuji mu id z neo4j, pokud uz tam existuje (aby se provedl update)
            CalendarNode calendarNodeInNeo4j = calendarNodeService.findByCalendarId(calendar.getId());
            if (calendarNodeInNeo4j != null) {
                calendarNode.setId(calendarNodeInNeo4j.getId());
            }

            //a calendarNode ulozim i se vsemi calendarDates
            calendarNodeService.save(calendarNode, -1);
        }
    }

    @Override
    @Transactional(value = "transactionManager")
    public void delete(String s, boolean neo4jSynchronize) {
        Calendar calendar = dao.find(s);
        if(calendar == null) return;

        //soucasne to musim smazat z neo4j
        if(neo4jSynchronize) {
            //TODO aktualizovat mapu ve vyhledavaci neo4j pres cypher!
            //nejdriv calendar dates
            calendarDateNodeService.deleteByCalendarId(calendar.getId());

            //potom calendar
            CalendarNode calendarNode = calendarNodeService.findByCalendarId(calendar.getId());
            if (calendarNode != null) {
                //mazu jen pokud v neo4j existuje
                calendarNodeService.delete(calendarNode);
            }
        }

        //a nakonec smazat z postgre
        dao.delete(s);
    }

    public static CalendarNode getCalendarNodeFromCalendar(Calendar calendar) {
        CalendarNode calendarNode = new CalendarNode();
        calendarNode.setCalendarId(calendar.getId());
        calendarNode.setFromDateInSeconds(getSecondsSinceEpoch(calendar.getStartDate()));
        calendarNode.setToDateInSeconds(getSecondsSinceEpoch(calendar.getEndDate()));

        calendarNode.setMonday(calendar.isMonday());
        calendarNode.setTuesday(calendar.isTuesday());
        calendarNode.setWednesday(calendar.isWednesday());
        calendarNode.setThursday(calendar.isThursday());
        calendarNode.setFriday(calendar.isFriday());
        calendarNode.setSaturday(calendar.isSaturday());
        calendarNode.setSunday(calendar.isSunday());

        List<CalendarDateNode> calendarDateNodes = new ArrayList<>();
        for(CalendarDate calendarDate : calendar.getCalendarDates()) {
            CalendarDateNode calendarDateNode = new CalendarDateNode();
            calendarDateNode.setCalendarDateId(calendarDate.getId());
            calendarDateNode.setDateInSeconds(getSecondsSinceEpoch(calendarDate.getDate()));
            calendarDateNode.setInclude(CalendarExceptionType.INCLUDE.equals(calendarDate.getExceptionType()));
            calendarDateNode.setCalendarNode(calendarNode);

            calendarDateNodes.add(calendarDateNode);
        }

        calendarNode.setCalendarDateNodes(calendarDateNodes);
        return calendarNode;
    }

    /**
     * @param localDate localDate
     * @return pocet vterin since epoch
     */
    private static long getSecondsSinceEpoch(LocalDate localDate) {
        return Date.valueOf(localDate).getTime() / 1000;
    }

}
