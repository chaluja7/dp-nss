package cz.cvut.dp.nss.persistence.route;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.route.Route;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of RouteDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
public class RouteDao extends AbstractGenericJpaDao<Route, String> {

    public RouteDao() {
        super(Route.class);
    }

}
