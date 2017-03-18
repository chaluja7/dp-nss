package cz.cvut.dp.nss.batch.output.shape;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class ShapeBatchWrapper {

    private String shapeId;

    private Double lat;

    private Double lon;

    private Integer sequence;

    public String getShapeId() {
        return shapeId;
    }

    public void setShapeId(String shapeId) {
        this.shapeId = shapeId;
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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
