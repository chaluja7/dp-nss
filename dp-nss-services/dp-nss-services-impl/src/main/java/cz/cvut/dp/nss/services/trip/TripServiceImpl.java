package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.persistence.trip.JdbcTripDao;
import cz.cvut.dp.nss.persistence.trip.TripDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import cz.cvut.dp.nss.services.stopTime.StopTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of TripService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class TripServiceImpl extends AbstractEntityService<Trip, String, TripDao> implements TripService {

    @Autowired
    private JdbcTripDao jdbcTripDao;

    private StopTimeService stopTimeService;

    @Autowired
    public TripServiceImpl(TripDao dao, StopTimeService stopTimeService) {
        super(dao);
        this.stopTimeService = stopTimeService;
    }

    @Override
    @Transactional(value = "transactionManager")
    public void update(Trip trip) {
        //nejdriv smazu vsechny stopTimes k tomuto zaznamu z db
        stopTimeService.deleteByTripId(trip.getId());

        //ulozim vsechny stopTimes znovu
        if(trip.getStopTimes() != null) {
            for(StopTime stopTime : trip.getStopTimes()) {
                stopTime.setId(null);
                stopTimeService.create(stopTime);
            }
        }

        //pak provedu update trip zaznamu
        dao.update(trip);

        //TODO update stopTimes a take v neo4j!
    }

    @Override
    @Transactional(value = "transactionManager")
    public void create(Trip trip) {
        //TODO create v neo4j
        dao.create(trip);
    }

    @Override
    @Transactional(value = "transactionManager")
    public void delete(String s) {
        //TODO delete v neo4j
        dao.delete(s);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TripWrapper> getAllForInsertToGraph() {
        return jdbcTripDao.getAllForInsertToGraph();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public long getCountByCalendarId(String calendarId) {
        return calendarId != null ? dao.getCountByCalendarId(calendarId) : 0;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Trip> getByFilter(TripFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc) {
        if(limit != null && limit <= 0) return new ArrayList<>();
        List<Trip> trips = dao.getByFilter(filter, getOffsetOrDefault(offset), getLimitOrDefault(limit), orderColumn != null ? orderColumn : "", asc);
        for(Trip trip : trips) {
            //lazy init routy a calendar
            if(trip.getCalendar() != null) trip.getCalendar().getId();
            if(trip.getRoute() != null) trip.getRoute().getId();
        }

        return trips;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public long getCountByFilter(TripFilter filter) {
        return dao.getCountByFilter(filter);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public Trip getLazyInitialized(String id) {
        return id != null ? dao.getLazyInitialized(id) : null;
    }

}
