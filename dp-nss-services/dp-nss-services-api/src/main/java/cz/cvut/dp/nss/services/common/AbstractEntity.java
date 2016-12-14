package cz.cvut.dp.nss.services.common;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Super class for all entities.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof AbstractEntity) {
            AbstractEntity other = (AbstractEntity) obj;
            return this.id != null && other.id != null && this.id.equals(other.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
