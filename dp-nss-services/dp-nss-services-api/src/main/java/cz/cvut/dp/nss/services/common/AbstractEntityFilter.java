package cz.cvut.dp.nss.services.common;

import java.io.Serializable;

/**
 * Common filter for entities.
 *
 * @author jakubchalupa
 * @since 11.03.17
 */
public abstract class AbstractEntityFilter<ID extends Serializable> {

    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}
