package cz.cvut.dp.nss.services.route;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.List;

/**
 * Common interface for all RouteService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface RouteService extends EntityService<Route, String> {

    /**
     * @param id id of route
     * @return route with agency if exists
     */
    Route getWithAgency(String id);

    /**
     * @param searchQuery retezec, ktery se vyskytuje v ID nebo nazvech routy
     * @return vsechny routy, kde id nebo nazvy odpovidaji searchQuery
     */
    List<Route> findRoutesBySearchQuery(String searchQuery);

    /**
     * @param filter filter
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @param orderColumn radici sloupec
     * @param asc true pokud radim asc, false jinak (desc)
     * @return vsechny routy dle filtru
     */
    List<Route> getByFilter(final RouteFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc);

    /**
     * @return celkovy pocet zaznamu dle filtru (pro ucely strankovani)
     */
    long getCountByFilter(final RouteFilter filter);

    /**
     * @param id id routy
     * @return true, pokud muze byt routa smazana (nema zadne tripy)
     */
    boolean canBeDeleted(String id);

}
