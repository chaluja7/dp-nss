package cz.cvut.dp.nss.batch.graph.trip;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNode;
import cz.cvut.dp.nss.graph.services.trip.TripNode;
import cz.cvut.dp.nss.services.stop.StopServiceImpl;
import cz.cvut.dp.nss.services.stopTime.StopTimeWrapper;
import cz.cvut.dp.nss.services.trip.TripWrapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphTripBatchProcessor")
public class GraphTripBatchProcessor implements ItemProcessor<TripWrapper, TripNode> {

    @Override
    public TripNode process(TripWrapper trip) throws Exception {
        TripNode tripNode = new TripNode();
        tripNode.setTripId(trip.getId());
        tripNode.setCalendarId(trip.getCalendarId());

        List<StopTimeNode> stopTimeNodes = new ArrayList<>();
        StopTimeNode firstStopTimeNode = null;
        StopTimeNode prevStopTimeNode = null;
        //pocitame s tim, ze stopTimes jsou serazene dle sequence VZESTUPNE!
        for(StopTimeWrapper stopTime : trip.getStopTimeWrappers()) {
            StopTimeNode stopTimeNode = new StopTimeNode();
            stopTimeNode.setStopTimeId(stopTime.getId());
            stopTimeNode.setStopId(stopTime.getStopId());
            stopTimeNode.setStopName(StopServiceImpl.getFixedStopName(stopTime.getStopName()));
            stopTimeNode.setSequence(stopTime.getSequence());
            stopTimeNode.setTripId(trip.getId());
            stopTimeNode.setTripNode(tripNode);

            if(prevStopTimeNode != null) {
                //zpetne priradim nextStop
                prevStopTimeNode.setNextStop(stopTimeNode);
            }

            //uzel nemusi mit departure/arrival, v tom pripade jej vezmu z predchoziho uzlu
            if(stopTime.getDeparture() != null) {
                stopTimeNode.setDepartureInSeconds(getSecondsOfDay(stopTime.getDeparture()));
            } else {
                //null to ale nemuze byt, pokud neni zadny predchozi uzel!
                assert(prevStopTimeNode != null);
                stopTimeNode.setDepartureInSeconds(prevStopTimeNode.getDepartureInSeconds());
            }

            if(stopTime.getArrival() != null) {
                stopTimeNode.setArrivalInSeconds(getSecondsOfDay(stopTime.getArrival()));
            } else {
                //null to ale nemuze byt, pokud neni zadny predchozi uzel!
                assert(prevStopTimeNode != null);
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

    /**
     * @param localTime java localTime
     * @return pocet sekund v ramci localTime
     */
    private static int getSecondsOfDay(LocalTime localTime) {
        return localTime.getSecond() + (localTime.getMinute() * 60) + (localTime.getHour() * 60 * 60);
    }

}
