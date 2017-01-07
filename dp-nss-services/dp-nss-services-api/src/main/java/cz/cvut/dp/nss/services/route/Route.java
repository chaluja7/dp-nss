package cz.cvut.dp.nss.services.route;

import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.common.AbstractAssignedIdEntity;
import cz.cvut.dp.nss.services.trip.Trip;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Entita, na kterou jsou navazene jednotlive tripy dane spolecnosti (agency)
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "routes",
    indexes = {@Index(name = "route_type_index", columnList = "routeType"),
        @Index(name = "route_agency_index", columnList = "agency_id")})
public class Route extends AbstractAssignedIdEntity {

    /**
     * kratke oznaceni
     */
    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String shortName;

    /**
     * dlouhe oznaceni
     */
    @Column
    @Size(max = 255)
    private String longName;

    /**
     * typ dopravniho prostredku
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RouteType routeType;

    /**
     * barva, ktera se bude pouzivat pri oznaceni
     */
    @Column
    @Size(max = 255)
    private String color;

    /**
     * spolecnost provozujici tuto routu
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    /**
     * tripy navazene na tuto routu
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "route")
    private List<Trip> trips;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public List<Trip> getTrips() {
        if(trips == null) {
            trips = new ArrayList<>();
        }

        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public void addTrip(Trip trip) {
        if(!getTrips().contains(trip)) {
            trips.add(trip);
        }

        trip.setRoute(this);
    }

}
