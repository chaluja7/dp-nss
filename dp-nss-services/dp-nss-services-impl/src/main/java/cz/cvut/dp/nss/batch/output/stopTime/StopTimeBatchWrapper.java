package cz.cvut.dp.nss.batch.output.stopTime;

import java.math.BigDecimal;

/**
 * Wrapper zastaveni pro export.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class StopTimeBatchWrapper {

    private BigDecimal id;

    private String tripId;

    private String stopId;

    private Integer sequence;

    private String arrival;

    private String departure;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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
