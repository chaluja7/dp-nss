package cz.cvut.dp.nss.persistence.shape;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.shape.Shape;
import cz.cvut.dp.nss.services.shape.ShapeId;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of ShapeDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
public class ShapeDao extends AbstractGenericJpaDao<Shape, ShapeId> {

    public ShapeDao() {
        super(Shape.class);
    }

}
