package cz.cvut.dp.nss.batch.input.shape;

import cz.cvut.dp.nss.services.shape.Shape;
import cz.cvut.dp.nss.services.shape.ShapeService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writer importu prujezdniho bodu.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "shapeBatchWriter")
public class ShapeBatchWriter implements ItemWriter<Shape> {

    @Autowired
    private ShapeService shapeService;

    @Override
    public void write(List<? extends Shape> items) throws Exception {
        for(Shape shape : items) {
            shapeService.create(shape);
        }
    }

}
