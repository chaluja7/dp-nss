package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import cz.cvut.dp.nss.services.ride.Ride;
import cz.cvut.dp.nss.services.station.Station;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * One stop of a ride.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "stops",
    indexes = {@Index(name = "stop_arrival_index", columnList = "arrival"),
        @Index(name = "stop_departure_index", columnList = "departure"),
        @Index(name = "stop_station_index", columnList = "station_id"),
        @Index(name = "stop_ride_index", columnList = "ride_id")})
public class Stop extends AbstractEntity {

    @Column
    @Type(type = "org.hibernate.type.LocalDateType")
    private LocalTime arrival;

    @Column
    @Type(type = "org.hibernate.type.LocalDateType")
    private LocalTime departure;

    @Column
    private Integer stopOrder;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ride_id")
    private Ride ride;

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

    public Integer getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(Integer stopOrder) {
        this.stopOrder = stopOrder;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

}
