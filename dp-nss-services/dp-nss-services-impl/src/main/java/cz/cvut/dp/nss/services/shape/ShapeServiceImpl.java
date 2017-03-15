package cz.cvut.dp.nss.services.shape;

import cz.cvut.dp.nss.persistence.shape.ShapeDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional(value = "transactionManager")
    public void deleteByShapeId(String id) {
        dao.deleteByShapeId(id);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Shape> getByShapeId(String id) {
        return id != null ? dao.getByShapeId(id) : new ArrayList<>();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<String> findShapeIdsByLikeId(String idPattern) {
        return dao.findShapeIdsByLikeId(idPattern);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<String> getShapeIdsByFilter(ShapeFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc) {
        if(limit != null && limit <= 0) return new ArrayList<>();
        //vytahnu si shapeId dle filtru (a uz serazene)
        return dao.getShapeIdsByFilter(filter, getOffsetOrDefault(offset), getLimitOrDefault(limit), orderColumn != null ? orderColumn : "", asc);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public long getCountByFilter(ShapeFilter filter) {
        return dao.getCountByFilter(filter);
    }
}
