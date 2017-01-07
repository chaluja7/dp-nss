package cz.cvut.dp.nss.services.shape;

import cz.cvut.dp.nss.persistence.shape.ShapeDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of ShapeService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class ShapeServiceImpl extends AbstractEntityService<Shape, ShapeId, ShapeDao> implements ShapeService {

    @Autowired
    public ShapeServiceImpl(ShapeDao dao) {
        super(dao);
    }

}
