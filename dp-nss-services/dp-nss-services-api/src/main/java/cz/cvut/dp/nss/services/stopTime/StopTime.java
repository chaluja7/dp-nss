package cz.cvut.dp.nss.services.stopTime;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.trip.Trip;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * Jedna ze zastavek tripu (zastaveni autobusu 135 na stanici Praha hl. n. v konkretnim case)
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "stop_times",
    indexes = {@Index(name = "stop_time_arrival_index", columnList = "arrival"),
        @Index(name = "stop_time_departure_index", columnList = "departure"),
        @Index(name = "stop_time_stop_index", columnList = "stop_id"),
        @Index(name = "stop_time_trip_index", columnList = "trip_id")})
public class StopTime extends AbstractEntity<Long> {

    /**
     * cas prijezdu
     */
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.LocalTimeType")
    @NotNull
    private LocalTime arrival;

    /**
     * cas odjezdu
     */
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.LocalTimeType")
    @NotNull
    private LocalTime departure;

    /**
     * poradi v ramci tripu
     */
    @Column(nullable = false)
    @NotNull
    private Integer sequence;

    /**
     * stanice, na ktere se zastavuje
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "stop_id")
    private Stop stop;

    /**
     * trip, kteremu patri toto zastaveni
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public LocalTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalTime arrival) {
        this.arrival = arrival;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
