package cz.cvut.dp.nss.wrapper.output.trip;

import cz.cvut.dp.nss.wrapper.output.route.RouteWrapper;

/**
 * @author jakubchalupa
 * @since 25.02.17
 */
public class TripWithRouteWrapper extends TripWrapper {

    private RouteWrapper route;

    public RouteWrapper getRoute() {
        return route;
    }

    public void setRoute(RouteWrapper route) {
        this.route = route;
    }
}
