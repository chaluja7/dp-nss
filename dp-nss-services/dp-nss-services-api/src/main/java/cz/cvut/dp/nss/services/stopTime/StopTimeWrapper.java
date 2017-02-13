package cz.cvut.dp.nss.services.stopTime;

import java.time.LocalTime;

/**
 * @author jakubchalupa
 * @since 12.02.17
 */
public class StopTimeWrapper {

    private Long id;

    private String stopId;

    private String stopName;

    private Integer sequence;

    private LocalTime departure;

    private LocalTime arrival;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    public LocalTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalTime arrival) {
        this.arrival = arrival;
    }
}
