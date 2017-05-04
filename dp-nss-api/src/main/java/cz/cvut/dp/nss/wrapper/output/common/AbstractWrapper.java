package cz.cvut.dp.nss.wrapper.output.common;

import java.io.Serializable;

/**
 * Predek vsech wrapperu z a do aplikace pres REST api.
 *
 * @author jakubchalupa
 * @since 12.03.17
 */
public abstract class AbstractWrapper<ID extends Serializable> {

    protected ID id;

    protected boolean canBeDeleted;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }
}