package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.stopTime.StopTimeWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper jizdy.
 *
 * @author jakubchalupa
 * @since 12.02.17
 */
public class TripWrapper {

    private String id;

    private String calendarId;

    private boolean wheelChair;

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

    public boolean isWheelChair() {
        return wheelChair;
    }

    public void setWheelChair(boolean wheelChair) {
        this.wheelChair = wheelChair;
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
