package cz.cvut.dp.nss.services.calendar;

import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import cz.cvut.dp.nss.services.common.AbstractAssignedIdEntity;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.trip.Trip;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Rozsah platnosti tripu.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "calendar")
public class Calendar extends AbstractAssignedIdEntity {

    /**
     * plati v pondeli?
     */
    @Column(nullable = false)
    private boolean monday;

    /**
     * plati v utery?
     */
    @Column(nullable = false)
    private boolean tuesday;

    /**
     * plati ve stredu?
     */
    @Column(nullable = false)
    private boolean wednesday;

    /**
     * plati ve ctvrtek?
     */
    @Column(nullable = false)
    private boolean thursday;

    /**
     * plati v patek?
     */
    @Column(nullable = false)
    private boolean friday;

    /**
     * plati v sobotu?
     */
    @Column(nullable = false)
    private boolean saturday;

    /**
     * plati v nedeli
     */
    @Column(nullable = false)
    private boolean sunday;

    /**
     * platnost od
     */
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.LocalDateType")
    @NotNull
    private LocalDate startDate;

    /**
     * platnost do
     */
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.LocalDateType")
    @NotNull
    private LocalDate endDate;

    /**
     * kolekce vyjimkovych datumu (kdy jede/nejede navic v intervalu startDate - endDate)
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "calendar")
    private List<CalendarDate> calendarDates;

    /**
     * tripy pro ktere plati tento interval platnosti
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "calendar")
    private List<Trip> trips;

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
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

    public List<CalendarDate> getCalendarDates() {
        if(calendarDates == null) {
            calendarDates = new ArrayList<>();
        }

        return calendarDates;
    }

    public void setCalendarDates(List<CalendarDate> calendarDates) {
        this.calendarDates = calendarDates;
    }

    public void addCalendarDate(CalendarDate calendarDate) {
        if(!getCalendarDates().contains(calendarDate)) {
            calendarDates.add(calendarDate);
        }

        calendarDate.setCalendar(this);
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

        trip.setCalendar(this);
    }

    @Override
    public String toString() {
        String oiName = getId() + " (";

        oiName += getStartDate().format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_PATTERN)) + " - " + getEndDate().format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_PATTERN));
        oiName += ") ";
        if(isMonday()) {
            oiName += "PO|";
        }
        if(isTuesday()) {
            oiName += "ÚT|";
        }
        if(isWednesday()) {
            oiName += "ST|";
        }
        if(isThursday()) {
            oiName += "ČT|";
        }
        if(isFriday()) {
            oiName += "PÁ|";
        }
        if(isSaturday()) {
            oiName += "SO|";
        }
        if(isSunday()) {
            oiName += "NE|";
        }

        return oiName;
    }
}
