package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.stopTime.StopTimeWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 12.02.17
 */
public class TripWrapper {

    private String id;

    private String calendarId;

    private List<StopTimeWrapper> stopTimeWrappers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public List<StopTimeWrapper> getStopTimeWrappers() {
        if(stopTimeWrappers == null) {
            stopTimeWrappers = new ArrayList<>();
        }

        return stopTimeWrappers;
    }

    public void setStopTimeWrappers(List<StopTimeWrapper> stopTimeWrappers) {
        this.stopTimeWrappers = stopTimeWrappers;
    }
}
