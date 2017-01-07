package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.common.AbstractAssignedIdEntity;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.stopTime.StopTime;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Jizda dopravniho prostredku v intervalu platnosti po route.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
@Entity
@Table(name = "trips",
    indexes = {@Index(name = "trip_route_index", columnList = "route_id"),
        @Index(name = "trip_calendar_index", columnList = "calendar_id")})
public class Trip extends AbstractAssignedIdEntity {

    /**
     * napis na ceduly dopravniho prostredku (napr. cislo busu, konecna stanice apod.)
     */
    @Column
    @Size(max = 255)
    private String headSign;

    /**
     * interval platnosti jizdy
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    /**
     * oznacena trasa teto jizdy
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "route_id")
    private Route route;

    /**
     * zastavky jizdy v konkretnich casech na konkretnich stanicich
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "trip")
    @OrderBy("sequence ASC")
    private List<StopTime> stopTimes;

    public String getHeadSign() {
        return headSign;
    }

    public void setHeadSign(String headSign) {
        this.headSign = headSign;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
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

        stopTime.setTrip(this);
    }
}
