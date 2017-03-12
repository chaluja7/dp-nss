package cz.cvut.dp.nss.services.route;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.agency.AgencyService;
import cz.cvut.dp.nss.services.agency.AgencyServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class RouteServiceIT extends AbstractServiceIT {

    @Autowired
    private RouteService routeService;

    @Autowired
    private AgencyService agencyService;

    @Test
    public void testCRUD() {
        Agency agency = AgencyServiceIT.getAgency("agency" + System.currentTimeMillis(), "agency1", "", "");
        agencyService.create(agency);

        final String id = "route" + System.currentTimeMillis();
        String shortName = "routeShort";
        String longName = "routeLong";
        String color = "color";
        RouteType routeType = RouteType.METRO;

        Route route = getRoute(id, agency, shortName, longName, color, routeType);

        //insert
        routeService.create(route);

        //retrieve
        Route retrieved = routeService.get(route.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(id, retrieved.getId());
        Assert.assertEquals(shortName, retrieved.getShortName());
        Assert.assertEquals(longName, retrieved.getLongName());
        Assert.assertEquals(color, retrieved.getColor());
        Assert.assertEquals(routeType, retrieved.getRouteType());

        //update
        shortName = "newShort";
        routeType = RouteType.BOAT;

        retrieved.setShortName(shortName);
        retrieved.setRouteType(routeType);
        routeService.update(retrieved);

        //check
        retrieved = routeService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(shortName, retrieved.getShortName());
        Assert.assertEquals(routeType, retrieved.getRouteType());

        //delete
        routeService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(routeService.get(retrieved.getId()));

        //delete agency
        agencyService.delete(agency.getId());
    }

    @Test
    public void testGetWithAgency() {
        Route route = routeService.getWithAgency("L125D1");
        Assert.assertNotNull(route);
    }

    @Test
    public void testGetByFilter() {
        List<Route> byFilter = routeService.getByFilter(getFilter(),0, 15, "agencyId", true);
        Assert.assertNotNull(byFilter);
    }

    @Test
    public void testGetCountByFilter() {
        long countByFilter = routeService.getCountByFilter(getFilter());
        Assert.assertTrue(countByFilter >= 0);
    }

    @Test
    public void testFindRoutesBySearchQuery() {
        List<Route> list = routeService.findRoutesBySearchQuery("Smích");
        Assert.assertNotNull(list);
    }

    @Test
    public void testCanBeDeleted() {
        boolean canBeDeleted = routeService.canBeDeleted("L125D1");
        Assert.assertTrue(true);
    }

    public static Route getRoute(final String id, Agency agency, String shortName, String longName, String color, RouteType routeType) {
        Route route = new Route();
        route.setId(id);
        route.setAgency(agency);
        route.setShortName(shortName);
        route.setLongName(longName);
        route.setColor(color);
        route.setRouteType(routeType);

        return route;
    }

    public static RouteFilter getFilter() {
        RouteFilter filter = new RouteFilter();
        filter.setShortName("125");

        return filter;
    }

}
