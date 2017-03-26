package cz.cvut.dp.nss.wrapper.output.stopTime;

import cz.cvut.dp.nss.wrapper.output.common.AbstractWrapper;
import cz.cvut.dp.nss.wrapper.output.stop.StopWrapper;

/**
 * @author jakubchalupa
 * @since 16.03.17
 */
public class StopTimeWrapper extends AbstractWrapper<Long> {

    private String arrival;

    private String departure;

    private Integer sequence;

    private String stopId;

    private StopWrapper stop;

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public StopWrapper getStop() {
        return stop;
    }

    public void setStop(StopWrapper stop) {
        this.stop = stop;
    }
}
