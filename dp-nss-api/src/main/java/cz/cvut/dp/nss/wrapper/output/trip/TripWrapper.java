package cz.cvut.dp.nss.wrapper.output.trip;

import cz.cvut.dp.nss.wrapper.output.calendar.CalendarWrapper;
import cz.cvut.dp.nss.wrapper.output.common.AbstractWrapper;
import cz.cvut.dp.nss.wrapper.output.route.RouteWrapper;
import cz.cvut.dp.nss.wrapper.output.shape.ShapeWrapper;
import cz.cvut.dp.nss.wrapper.output.stopTime.StopTimeWrapper;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 24.02.17
 */
public class TripWrapper extends AbstractWrapper<String> {

    private String headSign;

    private Integer wheelChairCode;

    private String shapeId;

    private String calendarId;

    private String routeId;

    private String routeShortName;

    private RouteWrapper route;

    private CalendarWrapper calendar;

    private List<StopTimeWrapper> stopTimes;

    private List<ShapeWrapper> shapes;

    public String getHeadSign() {
        return headSign;
    }

    public void setHeadSign(String headSign) {
        this.headSign = headSign;
    }

    public Integer getWheelChairCode() {
        return wheelChairCode;
    }

    public void setWheelChairCode(Integer wheelChairCode) {
        this.wheelChairCode = wheelChairCode;
    }

    public String getShapeId() {
        return shapeId;
    }

    public void setShapeId(String shapeId) {
        this.shapeId = shapeId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public List<StopTimeWrapper> getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(List<StopTimeWrapper> stopTimes) {
        this.stopTimes = stopTimes;
    }

    public RouteWrapper getRoute() {
        return route;
    }

    public void setRoute(RouteWrapper route) {
        this.route = route;
    }

    public CalendarWrapper getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarWrapper calendar) {
        this.calendar = calendar;
    }

    public List<ShapeWrapper> getShapes() {
        return shapes;
    }

    public void setShapes(List<ShapeWrapper> shapes) {
        this.shapes = shapes;
    }
}
