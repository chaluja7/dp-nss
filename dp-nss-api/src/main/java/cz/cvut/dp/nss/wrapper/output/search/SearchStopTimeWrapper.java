package cz.cvut.dp.nss.wrapper.output.search;

import cz.cvut.dp.nss.wrapper.output.stop.StopWrapper;
import cz.cvut.dp.nss.wrapper.output.trip.TripWithRouteWrapper;

/**
 * @author jakubchalupa
 * @since 24.02.17
 */
public class SearchStopTimeWrapper {

    /**
     * stanice
     */
    private StopWrapper stop;

    private TripWithRouteWrapper trip;

    private String arrival;

    private String departure;

    public StopWrapper getStop() {
        return stop;
    }

    public void setStop(StopWrapper stop) {
        this.stop = stop;
    }

    public TripWithRouteWrapper getTrip() {
        return trip;
    }

    public void setTrip(TripWithRouteWrapper trip) {
        this.trip = trip;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }
}
