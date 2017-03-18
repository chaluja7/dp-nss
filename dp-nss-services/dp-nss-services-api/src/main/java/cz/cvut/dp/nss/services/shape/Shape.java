package cz.cvut.dp.nss.services.shape;

import cz.cvut.dp.nss.services.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Prujezdni bod nejake trasy pro zobrazeni linky na mape
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Entity
@Table(name = "shapes", indexes = {@Index(name = "shape_sequence_id_index", columnList = "shapeId,sequence")})
public class Shape extends AbstractEntity<ShapeId> {

    @EmbeddedId
    private ShapeId id;

    /**
     * latitude
     */
    @Column(nullable = false)
    @NotNull
    @Min(-90)
    @Max(90)
    private Double lat;

    /**
     * longitude
     */
    @Column(nullable = false)
    @NotNull
    @Min(-180)
    @Max(180)
    private Double lon;

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

    @Override
    public ShapeId getId() {
        return id;
    }

    @Override
    public void setId(ShapeId shapeId) {
        this.id = shapeId;
    }

}
