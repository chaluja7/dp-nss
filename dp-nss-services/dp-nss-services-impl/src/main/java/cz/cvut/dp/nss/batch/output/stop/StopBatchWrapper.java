package cz.cvut.dp.nss.batch.output.stop;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class StopBatchWrapper {

    private String id;

    private String name;

    private Double lat;

    private Double lon;

    private String parentId;

    private Integer wheelChairType;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getWheelChairType() {
        return wheelChairType;
    }

    public void setWheelChairType(Integer wheelChairType) {
        this.wheelChairType = wheelChairType;
    }
}
