package cz.cvut.dp.nss.services.shape;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Embeddable ID shapu (id se sklada ze dvou sloupcu)
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Embeddable
public class ShapeId implements Serializable {

    /**
     * identifikator shapu
     */
    @Column
    private String shapeId;

    /**
     * poradi shapu v ramci shapeId
     */
    @Column
    private Integer sequence;

    public ShapeId() {
        //empty
    }

    public ShapeId(String shapeId, Integer sequence) {
        this.shapeId = shapeId;
        this.sequence = sequence;
    }

    public String getShapeId() {
        return shapeId;
    }

    public void setShapeId(String shapeId) {
        this.shapeId = shapeId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ShapeId) {
            ShapeId other = (ShapeId) obj;
            return this.getShapeId() != null && this.getSequence() != null && this.getShapeId().equals(other.getShapeId()) && this.getSequence().equals(other.getSequence());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return 37 * hash + (this.getShapeId() != null ? this.getShapeId().hashCode() : 0) + (this.getSequence() != null ? this.getSequence().hashCode() : 0);
    }

}
