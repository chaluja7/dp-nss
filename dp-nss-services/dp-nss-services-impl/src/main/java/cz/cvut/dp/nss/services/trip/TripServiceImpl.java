package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNode;
import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNodeService;
import cz.cvut.dp.nss.graph.services.trip.TripNode;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
import cz.cvut.dp.nss.persistence.trip.JdbcTripDao;
import cz.cvut.dp.nss.persistence.trip.TripDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.stop.StopServiceImpl;
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

    private TripNodeService tripNodeService;

    private StopTimeNodeService stopTimeNodeService;

    @Autowired
    public TripServiceImpl(TripDao dao, StopTimeService stopTimeService, TripNodeService tripNodeService, StopTimeNodeService stopTimeNodeService) {
        super(dao);
        this.stopTimeService = stopTimeService;
        this.tripNodeService = tripNodeService;
        this.stopTimeNodeService = stopTimeNodeService;
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
        //TODO update v neo4j!
    }

    @Override
    @Transactional(value = "transactionManager")
    public void create(Trip trip) {
        dao.create(trip);

        //soucasne musim data ulozit do neo4j!
        //vytvorim si objekt pro neo4j
        TripNode tripNode = getTripNodeFromTrip(trip);
        //a ulozim vsechno naraz
        tripNodeService.save(tripNode, -1);

        //jenze pozor, jeste je nutne stopTimes spravne zakomponovat do struktury grafu v ramci stanice
        //tedy najit spravne misto, kam kazdy stopTime prijde a tam ho napojit

        if(tripNode.getStopTimeNodes() != null) {
            for(StopTimeNode stopTimeNode : tripNode.getStopTimeNodes()) {
                stopTimeNodeService.addNewStopTimeToStop(stopTimeNode);
            }
        }

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

    public static TripNode getTripNodeFromTrip(Trip trip) {
        TripNode tripNode = new TripNode();
        tripNode.setTripId(trip.getId());
        if(trip.getCalendar() != null) tripNode.setCalendarId(trip.getCalendar().getId());

        List<StopTimeNode> stopTimeNodes = new ArrayList<>();
        StopTimeNode firstStopTimeNode = null;
        StopTimeNode prevStopTimeNode = null;
        //pocitame s tim, ze stopTimes jsou serazene dle sequence VZESTUPNE!
        for(StopTime stopTime : trip.getStopTimes()) {
            StopTimeNode stopTimeNode = new StopTimeNode();
            stopTimeNode.setStopTimeId(stopTime.getId());
            if(stopTime.getStop() != null) {
                stopTimeNode.setStopId(stopTime.getStop().getId());
                stopTimeNode.setStopName(StopServiceImpl.getFixedStopName(stopTime.getStop().getName()));
            }
            stopTimeNode.setSequence(stopTime.getSequence());
            stopTimeNode.setTripId(trip.getId());
            stopTimeNode.setTripNode(tripNode);

            if(prevStopTimeNode != null) {
                //zpetne priradim nextStop
                prevStopTimeNode.setNextStop(stopTimeNode);
            }

            //uzel nemusi mit departure/arrival, v tom pripade jej vezmu z predchoziho uzlu
            if(stopTime.getDeparture() != null) {
                stopTimeNode.setDepartureInSeconds(DateTimeUtils.getSecondsOfDay(stopTime.getDeparture()));
            } else {
                //null to ale nemuze byt, pokud neni zadny predchozi uzel!
                assert(prevStopTimeNode != null) : "neni vyplneny zadny cas odjezdu";
                stopTimeNode.setDepartureInSeconds(prevStopTimeNode.getDepartureInSeconds());
            }

            if(stopTime.getArrival() != null) {
                stopTimeNode.setArrivalInSeconds(DateTimeUtils.getSecondsOfDay(stopTime.getArrival()));
            } else {
                //null to ale nemuze byt, pokud neni zadny predchozi uzel!
                assert(prevStopTimeNode != null) : "neni vyplneny zadny cas prijezdu";
                stopTimeNode.setArrivalInSeconds(prevStopTimeNode.getArrivalInSeconds());
            }

            stopTimeNode.setOverMidnightDepartureInTrip(false);
            if(firstStopTimeNode != null) {
                //dle prvniho urcuji, zda je aktualni pres pulnoc s departureTime
                if(firstStopTimeNode.getDepartureOrArrivalInSeconds() > stopTimeNode.getDepartureOrArrivalInSeconds()) {
                    stopTimeNode.setOverMidnightDepartureInTrip(true);
                }
            }

            stopTimeNodes.add(stopTimeNode);
            if(firstStopTimeNode == null) {
                firstStopTimeNode = stopTimeNode;
            }
            prevStopTimeNode = stopTimeNode;
        }

        tripNode.setStopTimeNodes(stopTimeNodes);
        return tripNode;
    }

}
