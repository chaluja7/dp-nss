package cz.cvut.dp.nss.services.route;

import cz.cvut.dp.nss.persistence.route.RouteDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public Route getWithAgency(String id) {
        Route route = get(id);
        //lazy initialization
        if(route != null && route.getAgency() != null) route.getAgency().getId();

        return route;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Route> findRoutesBySearchQuery(String searchQuery) {
        if(searchQuery == null || searchQuery.length() < 1) return new ArrayList<>();
        List<Route> routes = dao.findRoutesBySearchQuery(searchQuery);
        for(Route route : routes) {
            //lazy initialization
           if(route.getAgency() != null) route.getAgency().getId();
        }

        return routes;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Route> getByFilter(final RouteFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc) {
        if(limit != null && limit <= 0) return new ArrayList<>();
        List<Route> routes = dao.getByFilter(filter, getOffsetOrDefault(offset), getLimitOrDefault(limit), orderColumn != null ? orderColumn : "", asc);
        for(Route route : routes) {
            //lazy initialization
            if(route.getAgency() != null) route.getAgency().getId();
        }

        return routes;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public long getCountByFilter(final RouteFilter filter) {
        return dao.getCountByFilter(filter);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean canBeDeleted(String id) {
        Route route = get(id);
        return route != null && route.getTrips().isEmpty();
    }
}
