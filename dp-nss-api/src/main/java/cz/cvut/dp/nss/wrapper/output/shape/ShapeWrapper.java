package cz.cvut.dp.nss.wrapper.output.shape;

/**
 * Objekt prujezdniho bodu.
 *
 * @author jakubchalupa
 * @since 15.03.17
 */
public class ShapeWrapper {

    private Integer sequence;

    private Double lat;

    private Double lon;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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
}
