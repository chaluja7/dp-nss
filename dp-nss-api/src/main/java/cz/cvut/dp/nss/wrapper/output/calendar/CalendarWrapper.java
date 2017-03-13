package cz.cvut.dp.nss.wrapper.output.calendar;

import cz.cvut.dp.nss.wrapper.output.calendarDate.CalendarDateWrapper;
import cz.cvut.dp.nss.wrapper.output.common.AbstractWrapper;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 13.03.17
 */
public class CalendarWrapper extends AbstractWrapper<String> {

    private String startDate;

    private String endDate;

    private boolean monday;

    private boolean tuesday;

    private boolean wednesday;

    private boolean thursday;

    private boolean friday;

    private boolean saturday;

    private boolean sunday;

    private List<CalendarDateWrapper> calendarDates;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

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

    public List<CalendarDateWrapper> getCalendarDates() {
        return calendarDates;
    }

    public void setCalendarDates(List<CalendarDateWrapper> calendarDates) {
        this.calendarDates = calendarDates;
    }
}
