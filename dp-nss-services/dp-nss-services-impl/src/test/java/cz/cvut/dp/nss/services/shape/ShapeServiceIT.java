package cz.cvut.dp.nss.services.shape;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class ShapeServiceIT extends AbstractServiceIT {

    @Autowired
    private ShapeService shapeService;

    @Test
    public void testCRUD() {
        final ShapeId id = new ShapeId("shape" + System.currentTimeMillis(), 1);
        Double lat = 40.0;
        Double lon = 50.0;

        Shape shape = getShape(id, lat, lon);

        //insert
        shapeService.create(shape);

        //retrieve
        Shape retrieved = shapeService.get(shape.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(id, retrieved.getId());
        Assert.assertEquals(lat, retrieved.getLat());
        Assert.assertEquals(lon, retrieved.getLon());

        //update
        lat = 30.0;
        retrieved.setLat(lat);
        shapeService.update(retrieved);

        //check
        retrieved = shapeService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(lat, retrieved.getLat());

        //delete
        shapeService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(shapeService.get(retrieved.getId()));
    }

    @Test
    public void testGetByShapeId() {
        List<Shape> byShapeId = shapeService.getByShapeId("1");
        Assert.assertNotNull(byShapeId);
    }

    @Test
    public void testFindShapesByLikeId() {
        List<String> shapeIdsByLikeId = shapeService.findShapeIdsByLikeId("1");
        Assert.assertNotNull(shapeIdsByLikeId);
    }

    @Test
    public void testGetByFilter() {
        List<String> shapeIdsByFilter = shapeService.getShapeIdsByFilter(getFilter(), 0, 15, "id", true);
        Assert.assertNotNull(shapeIdsByFilter);
    }

    @Test
    public void testGetCountByFilter() {
        long countByFilter = shapeService.getCountByFilter(getFilter());
        Assert.assertTrue(true);
    }

    public static Shape getShape(final ShapeId id, Double lat, Double lon) {
        Shape shape = new Shape();
        shape.setId(id);
        shape.setLat(lat);
        shape.setLon(lon);

        return shape;
    }

    private ShapeFilter getFilter() {
        ShapeFilter filter = new ShapeFilter();
//        filter.setId("1");

        return filter;
    }

}
