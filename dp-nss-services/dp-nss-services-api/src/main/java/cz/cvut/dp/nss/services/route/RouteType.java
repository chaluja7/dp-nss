package cz.cvut.dp.nss.services.route;

/**
 * Existujici prepravni prostredky.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public enum RouteType {

    /**
     * tramvaj
     */
    TRAM(0),

    /**
     * metro
     */
    METRO(1),

    /**
     * vlak
     */
    RAIL(2),

    /**
     * autobus
     */
    BUS(3),

    /**
     * lod - privoz
     */
    BOAT(4),

    /**
     * lanovka
     */
    CABLE_CAR(5),

    /**
     * taky lanovka
     */
    GONDOLA(6),

    /**
     * taky lanovka
     */
    FUNICULAR(7);

    private final int routeTypeCode;

    RouteType(int routeTypeCode) {
        this.routeTypeCode = routeTypeCode;
    }

    public int getRouteTypeCode() {
        return routeTypeCode;
    }
}
