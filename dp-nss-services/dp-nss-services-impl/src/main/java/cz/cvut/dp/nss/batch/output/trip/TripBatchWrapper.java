package cz.cvut.dp.nss.batch.output.trip;

/**
 * Wrapper jizdy pro export.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class TripBatchWrapper {

    private String id;

    private String headSign;

    private String shapeId;

    private String calendarId;

    private String routeId;

    private Integer wheelChairType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadSign() {
        return headSign;
    }

    public void setHeadSign(String headSign) {
        this.headSign = headSign;
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

    public Integer getWheelChairType() {
        return wheelChairType;
    }

    public void setWheelChairType(Integer wheelChairType) {
        this.wheelChairType = wheelChairType;
    }
}
