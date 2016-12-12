package cz.cvut.dp.nss.services.operationInterval;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.ride.Ride;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * OperationInterval entity.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "operation_intervals")
public class OperationInterval extends AbstractEntity {

    @Column
    private Boolean monday;

    @Column
    private Boolean tuesday;

    @Column
    private Boolean wednesday;

    @Column
    private Boolean thursday;

    @Column
    private Boolean friday;

    @Column
    private Boolean saturday;

    @Column
    private Boolean sunday;

    @Column
    @Type(type = "org.hibernate.type.LocalDateType")
    private LocalDate startDate;

    @Column
    @Type(type = "org.hibernate.type.LocalDateType")
    private LocalDate endDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "operationInterval")
    private List<Ride> rides;

    public Boolean getMonday() {
        return monday;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getFriday() {
        return friday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Ride> getRides() {
        if(rides == null) {
            rides = new ArrayList<>();
        }

        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public void addRide(Ride ride) {
        if(!getRides().contains(ride)) {
            getRides().add(ride);
        }

        ride.setOperationInterval(this);
    }

    @Override
    public String toString() {
        String oiName = getId() + " (";

        oiName += getStartDate().format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_PATTERN)) + " - " + getEndDate().format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_PATTERN));
        oiName += ") ";
        if(getMonday()) {
            oiName += "PO|";
        }
        if(getTuesday()) {
            oiName += "ÚT|";
        }
        if(getWednesday()) {
            oiName += "ST|";
        }
        if(getThursday()) {
            oiName += "ČT|";
        }
        if(getFriday()) {
            oiName += "PÁ|";
        }
        if(getSaturday()) {
            oiName += "SO|";
        }
        if(getSunday()) {
            oiName += "NE|";
        }

        return oiName;
    }
}
