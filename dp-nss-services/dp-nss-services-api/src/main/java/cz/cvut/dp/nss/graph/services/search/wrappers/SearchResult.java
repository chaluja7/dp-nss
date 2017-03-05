package cz.cvut.dp.nss.graph.services.search.wrappers;

import java.util.List;

/**
 * Wrapper to wrap search results.
 *
 * @author jakubchalupa
 * @since 06.12.14
 */
public class SearchResult {

    private long travelTime;

    private long departure;

    private long arrival;

    private boolean overMidnightDeparture;

    private boolean overMidnightArrival;

    private int numberOfTransfers;

    private List<Long> stopTimes;

    public long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(long travelTime) {
        this.travelTime = travelTime;
    }

    public long getDeparture() {
        return departure;
    }

    public void setDeparture(long departure) {
        this.departure = departure;
    }

    public long getArrival() {
        return arrival;
    }

    public void setArrival(long arrival) {
        this.arrival = arrival;
    }

    public boolean isOverMidnightDeparture() {
        return overMidnightDeparture;
    }

    public void setOverMidnightDeparture(boolean overMidnightDeparture) {
        this.overMidnightDeparture = overMidnightDeparture;
    }

    public boolean isOverMidnightArrival() {
        return overMidnightArrival;
    }

    public void setOverMidnightArrival(boolean overMidnightArrival) {
        this.overMidnightArrival = overMidnightArrival;
    }

    public int getNumberOfTransfers() {
        return numberOfTransfers;
    }

    public void setNumberOfTransfers(int numberOfTransfers) {
        this.numberOfTransfers = numberOfTransfers;
    }

    public List<Long> getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(List<Long> stopTimes) {
        this.stopTimes = stopTimes;
    }
}
