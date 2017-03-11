package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.common.AbstractEntityFilter;

/**
 * @author jakubchalupa
 * @since 11.03.17
 */
public class StopFilter extends AbstractEntityFilter<String> {

    private String name;

    private Double lat;

    private Double lon;

    private Integer wheelChairCode;

    private String parentStopId;

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
}
