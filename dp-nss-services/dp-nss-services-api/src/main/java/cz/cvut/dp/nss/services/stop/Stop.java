package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.common.AbstractAssignedIdEntity;
import cz.cvut.dp.nss.services.stopTime.StopTime;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Stanice (napr. Praha hl. n.)
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "stops",
    indexes = {@Index(name = "stop_parent_index", columnList = "parent_stop_id")})
public class Stop extends AbstractAssignedIdEntity {

    /**
     * nazev, opravdu neni schvalne unikatni (gtfs povoluje duplicity v ramci datasetu)
     */
    @Column
    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    /**
     * latitude
     */
    @Column(nullable = false)
    @NotNull
    @Min(-90)
    @Max(90)
    private Double lat;

    /**
     * longitude
     */
    @Column(nullable = false)
    @NotNull
    @Min(-180)
    @Max(180)
    private Double lon;

    /**
     * dostupnost stanice pro vozickare
     */
    @Column(name = "wheelchair")
    @Enumerated(EnumType.STRING)
    private StopWheelchairBoardingType stopWheelchairBoardingType;

    /**
     * nadrazena stanice (pokud je tato v nejakem komplexu)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_stop_id")
    private Stop parentStop;

    /**
     * list podrizenych stanic teto
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "parentStop")
    private List<Stop> childStops;

    /**
     * vsechna zastaveni na teto stanici
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "stop")
    private List<StopTime> stopTimes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public StopWheelchairBoardingType getStopWheelchairBoardingType() {
        return stopWheelchairBoardingType;
    }

    public void setStopWheelchairBoardingType(StopWheelchairBoardingType stopWheelchairBoardingType) {
        this.stopWheelchairBoardingType = stopWheelchairBoardingType;
    }

    public Stop getParentStop() {
        return parentStop;
    }

    public void setParentStop(Stop parentStop) {
        this.parentStop = parentStop;
    }

    public List<Stop> getChildStops() {
        if(childStops == null) {
            childStops = new ArrayList<>();
        }

        return childStops;
    }

    public void setChildStops(List<Stop> childStops) {
        this.childStops = childStops;
    }

    public void addChildStop(Stop stop) {
        if(getChildStops().contains(stop)) {
            childStops.add(stop);
        }

        stop.setParentStop(this);
    }

    public List<StopTime> getStopTimes() {
        if(stopTimes == null) {
            stopTimes = new ArrayList<>();
        }

        return stopTimes;
    }

    public void setStopTimes(List<StopTime> stopTimes) {
        this.stopTimes = stopTimes;
    }

    public void addStopTime(StopTime stopTime) {
        if(!getStopTimes().contains(stopTime)) {
            stopTimes.add(stopTime);
        }

        stopTime.setStop(this);

    }
}
