package cz.cvut.dp.nss.wrapper.output.shape;

import cz.cvut.dp.nss.wrapper.output.common.AbstractWrapper;

import java.util.List;

/**
 * Objekt prujezdnich bodu.
 *
 * @author jakubchalupa
 * @since 15.03.17
 */
public class ShapesWrapper extends AbstractWrapper<String> {

    private List<ShapeWrapper> shapes;

    public List<ShapeWrapper> getShapes() {
        return shapes;
    }

    public void setShapes(List<ShapeWrapper> shapes) {
        this.shapes = shapes;
    }
}
