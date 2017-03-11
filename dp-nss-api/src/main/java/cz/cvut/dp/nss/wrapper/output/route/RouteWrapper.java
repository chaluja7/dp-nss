package cz.cvut.dp.nss.wrapper.output.route;

/**
 * @author jakubchalupa
 * @since 25.02.17
 */
public class RouteWrapper {

    private String shortName;

    private String longName;

    private String routeType;

    private String color;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
