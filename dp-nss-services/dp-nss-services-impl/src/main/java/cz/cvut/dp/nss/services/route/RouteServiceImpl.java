package cz.cvut.dp.nss.services.route;

import cz.cvut.dp.nss.persistence.route.RouteDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of RouteService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class RouteServiceImpl extends AbstractEntityService<Route, String, RouteDao> implements RouteService {

    @Autowired
    public RouteServiceImpl(RouteDao dao) {
        super(dao);
    }

}
