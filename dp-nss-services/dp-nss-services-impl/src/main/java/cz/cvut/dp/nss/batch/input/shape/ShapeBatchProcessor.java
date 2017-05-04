package cz.cvut.dp.nss.batch.input.shape;

import cz.cvut.dp.nss.services.shape.Shape;
import cz.cvut.dp.nss.services.shape.ShapeId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Processor importu prujezdniho bodu.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "shapeBatchProcessor")
public class ShapeBatchProcessor implements ItemProcessor<DefaultFieldSet, Shape> {

    @Override
    public Shape process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        Shape shape = new Shape();
        shape.setId(new ShapeId((String) properties.get("shape_id"), Integer.parseInt((String) properties.get("shape_pt_sequence"))));
        shape.setLat(Double.parseDouble((String) properties.get("shape_pt_lat")));
        shape.setLon(Double.parseDouble((String) properties.get("shape_pt_lon")));

        return shape;
    }

}
