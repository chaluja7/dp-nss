package cz.cvut.dp.nss.controller.admin.route;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.admin.agency.AdminAgencyController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.agency.AgencyService;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.route.RouteFilter;
import cz.cvut.dp.nss.services.route.RouteService;
import cz.cvut.dp.nss.services.route.RouteType;
import cz.cvut.dp.nss.wrapper.output.route.RouteWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 12.03.17
 */
@RestController
@RequestMapping(value = "/admin/route")
public class AdminRouteController extends AdminAbstractController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private AgencyService agencyService;

    private static final String FILTER_SHORT_NAME = "shortName";
    private static final String FILTER_LONG_NAME = "longName";
    private static final String FILTER_TYPE = "typeCode";
    private static final String FILTER_COLOR = "color";
    private static final String FILTER_AGENCY_ID = "agencyId";

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<RouteWrapper>> getRoutes(@RequestHeader(value = X_LIMIT_HEADER, required = false) Integer xLimit,
                                      @RequestHeader(value = X_OFFSET_HEADER, required = false) Integer xOffset,
                                      @RequestHeader(value = X_ORDER_HEADER, required = false) String xOrder,
                                      @RequestParam(value = FILTER_ID, required = false) String id,
                                      @RequestParam(value = FILTER_SHORT_NAME, required = false) String shortName,
                                      @RequestParam(value = FILTER_LONG_NAME, required = false) String longName,
                                      @RequestParam(value = FILTER_TYPE, required = false) Integer typeCode,
                                      @RequestParam(value = FILTER_COLOR, required = false) String color,
                                      @RequestParam(value = FILTER_AGENCY_ID, required = false) String agencyId,
                                      @RequestParam(value = FILTER_SEARCH_QUERY, required = false) String searchQuery) throws BadRequestException {

        List<Route> routes;
        List<RouteWrapper> wrappers = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        if(!StringUtils.isBlank(searchQuery)) {
            //searchQuery vsechno prebiji a nic jineho se neaplikuje
            routes = routeService.findRoutesBySearchQuery(searchQuery);
        } else {
            final OrderWrapper order = getOrderFromHeader(xOrder);
            final RouteFilter filter = getFilterFromParams(id, shortName, longName, typeCode, color, agencyId);
            routes = routeService.getByFilter(filter, xOffset, xLimit, order.getOrderColumn(), order.isAsc());
            httpHeaders.add(X_COUNT_HEADER, routeService.getCountByFilter(filter) + "");
        }

        for(Route route : routes) {
            wrappers.add(getRouteWrapper(route, true));
        }

        return new ResponseEntity<>(wrappers, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RouteWrapper getRoute(@PathVariable("id") String id) {
        Route route = routeService.getWithAgency(id);
        if(route == null) throw new ResourceNotFoundException();

        RouteWrapper routeWrapper = getRouteWrapper(route, true);
        if(routeService.canBeDeleted(route.getId())) routeWrapper.setCanBeDeleted(true);
        return routeWrapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RouteWrapper> createRoute(@RequestBody RouteWrapper wrapper) throws BadRequestException {
        Route route = getRoute(wrapper);
        routeService.create(route);

        return getResponseCreated(getRouteWrapper(routeService.getWithAgency(route.getId()), true));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public RouteWrapper updateRoute(@PathVariable("id") String id, @RequestBody RouteWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        Route existingRoute = routeService.get(id);
        if(existingRoute == null) throw new ResourceNotFoundException();

        Route route = getRoute(wrapper);
        route.setId(id);
        routeService.update(route);

        return getRouteWrapper(route, true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteRoute(@PathVariable("id") String id) throws BadRequestException {
        Route route = routeService.get(id);
        if(route == null) {
            //ok, jiz neni v DB
            return;
        }

        if(!routeService.canBeDeleted(route.getId())) {
            throw new BadRequestException("route can not be deleted");
        }

        routeService.delete(route.getId());
    }

    public static RouteWrapper getRouteWrapper(Route route, boolean withAgency) {
        if(route == null) return null;

        RouteWrapper wrapper = new RouteWrapper();
        wrapper.setId(route.getId());
        wrapper.setShortName(route.getShortName());
        wrapper.setLongName(route.getLongName());
        wrapper.setTypeCode(route.getRouteType() != null ? route.getRouteType().getCode() : null);
        wrapper.setColor(route.getColor());
        if(withAgency) {
            wrapper.setAgencyId(route.getAgency() != null ? route.getAgency().getId() : null);
            wrapper.setAgencyName(route.getAgency() != null ? route.getAgency().getName() : null);
            wrapper.setAgency(AdminAgencyController.getAgencyWrapper(route.getAgency()));
        }

        return wrapper;
    }

    private Route getRoute(RouteWrapper wrapper) throws BadRequestException {
        if(wrapper == null) return null;

        Route route = new Route();
        route.setId(wrapper.getId());
        route.setShortName(wrapper.getShortName());
        route.setLongName(wrapper.getLongName());
        if(wrapper.getTypeCode() != null) {
            route.setRouteType(EnumUtils.fromCode(wrapper.getTypeCode(), RouteType.class));
        }
        route.setColor(wrapper.getColor());
        if(!StringUtils.isEmpty(wrapper.getAgencyId())) {
            Agency agency = agencyService.get(wrapper.getAgencyId());
            if(agency == null) {
                throw new BadRequestException("unknown agency");
            }
            route.setAgency(agency);
        }

        return route;
    }

    private static RouteFilter getFilterFromParams(String id, String shortName, String longName, Integer typeCode, String color, String agencyId) {
        RouteFilter routeFilter = new RouteFilter();
        routeFilter.setId(id);
        routeFilter.setShortName(shortName);
        routeFilter.setLongName(longName);
        routeFilter.setTypeCode(typeCode);
        routeFilter.setColor(color);
        routeFilter.setAgencyId(agencyId);

        return routeFilter;
    }
}
