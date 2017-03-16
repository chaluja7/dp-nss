package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.common.AbstractEntityFilter;

/**
 * @author jakubchalupa
 * @since 16.03.17
 */
public class TripFilter extends AbstractEntityFilter<String> {

    private String headSign;

    private Integer wheelChairCode;

    private String shapeId;

    private String calendarId;

    private String routeShortName;

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

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }
}
