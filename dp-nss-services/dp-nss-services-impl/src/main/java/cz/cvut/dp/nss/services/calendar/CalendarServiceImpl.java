package cz.cvut.dp.nss.services.calendar;

import cz.cvut.dp.nss.persistence.calendar.CalendarDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public CalendarServiceImpl(CalendarDao dao, TripService tripService) {
        super(dao);
        this.tripService = tripService;
    }

    @Override
    @Transactional(value = "transactionManager")
    public void update(Calendar entity) {
        //TODO, update calendarDates + neo4j
        super.update(entity);
    }

    @Override
    @Transactional(value = "transactionManager")
    public void create(Calendar entity) {
        //TODO
        super.create(entity);
    }

    @Override
    @Transactional(value = "transactionManager")
    public void delete(String s) {
        //TODO
        super.delete(s);
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
        return dao.getByFilter(filter, getOffsetOrDefault(offset), getLimitOrDefault(limit), orderColumn != null ? orderColumn : "", asc);    }

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

}
