package cz.cvut.dp.nss.services.route;

import cz.cvut.dp.nss.services.common.DomainCode;

/**
 * Existujici prepravni prostredky.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public enum RouteType implements DomainCode {

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

    private final int code;

    RouteType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
