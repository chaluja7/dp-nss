package cz.cvut.dp.nss.services.station;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import cz.cvut.dp.nss.services.region.Region;
import cz.cvut.dp.nss.services.routeStop.RouteStop;
import cz.cvut.dp.nss.services.stop.Stop;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * Station.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "stations", indexes = {@Index(name = "station_region_index", columnList = "region_id")})
public class Station extends AbstractEntity {

    @Column(unique = true)
    @NotBlank
    private String title;

    @Column(unique = true)
    @NotBlank
    private String name;

    @Column
    @Min(-90)
    @Max(90)
    private Double latitude;

    @Column
    @Min(-180)
    @Max(180)
    private Double longtitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "station")
    private List<RouteStop> routeStops;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "station")
    private List<Stop> stops;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<RouteStop> getRouteStops() {
        if(routeStops == null) {
            routeStops = new ArrayList<>();
        }

        return routeStops;
    }

    public void setRouteStops(List<RouteStop> routeStops) {
        this.routeStops = routeStops;
    }

    public List<Stop> getStops() {
        if(stops == null) {
            stops = new ArrayList<>();
        }

        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}
