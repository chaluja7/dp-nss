package cz.cvut.dp.nss.services.common;

import java.io.Serializable;

/**
 * Super class for all entities.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    public abstract ID getId();

    public abstract void setId(ID id);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof AbstractEntity) {
            AbstractEntity other = (AbstractEntity) obj;
            return this.getId() != null && other.getId() != null && this.getId().equals(other.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return 37 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
    }
}
