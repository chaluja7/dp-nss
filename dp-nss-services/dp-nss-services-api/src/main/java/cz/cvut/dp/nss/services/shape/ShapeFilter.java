package cz.cvut.dp.nss.services.shape;

import cz.cvut.dp.nss.services.common.AbstractEntityFilter;

/**
 * Filtr prujezdnich bodu.
 *
 * @author jakubchalupa
 * @since 12.03.17
 */
public class ShapeFilter extends AbstractEntityFilter<String> {

    private String exactId;

    public String getExactId() {
        return exactId;
    }

    public void setExactId(String exactId) {
        this.exactId = exactId;
    }
}
