package cz.cvut.dp.nss.wrapper.out.stop;

/**
 * @author jakubchalupa
 * @since 24.02.17
 */
public class StopWrapper {

    private String id;

    private String name;

    private Double lat;

    private Double lon;

    private Integer wheelChairCode;

    private String parentStopId;

    private boolean canBeDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getWheelChairCode() {
        return wheelChairCode;
    }

    public void setWheelChairCode(Integer wheelChairCode) {
        this.wheelChairCode = wheelChairCode;
    }

    public String getParentStopId() {
        return parentStopId;
    }

    public void setParentStopId(String parentStopId) {
        this.parentStopId = parentStopId;
    }

    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }
}
