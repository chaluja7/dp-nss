package cz.cvut.dp.nss.services.route;

import java.util.List;

/**
 * Common interface for all RouteService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface RouteService {

    /**
     * find route by id
     * @param id id of a route
     * @return route by id or null
     */
    public Route getRoute(long id);

    /**
     * find route by id with pre-loaded lazy initialization
     * @param id id of a route
     * @return route by id or null
     */
    public Route getRouteLazyInitialized(long id);

    /**
     * update route
     * @param route route to update
     * @return updated route
     */
    public Route updateRoute(Route route);

    /**
     * persists route
     * @param route route to persist
     */
    public void createRoute(Route route);

    /**
     * delete route
     * @param id id of route to delete
     */
    void deleteRoute(long id);

    /**
     * find all routes
     * @return all routes
     */
    public List<Route> getAll();

}
