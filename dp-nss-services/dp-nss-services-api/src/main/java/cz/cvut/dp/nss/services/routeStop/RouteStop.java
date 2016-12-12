package cz.cvut.dp.nss.services.routeStop;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.station.Station;

import javax.persistence.*;

/**
 * One stop on Route.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "route_stops",
    indexes = {@Index(name = "route_stop_station_index", columnList = "station_id"),
        @Index(name = "route_stop_route_index", columnList = "route_id")})
public class RouteStop extends AbstractEntity {

    @Column
    private Integer distance;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "route_id")
    private Route route;

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

}
