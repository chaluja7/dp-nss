package cz.cvut.dp.nss.wrapper.output.route;

import cz.cvut.dp.nss.wrapper.output.common.AbstractWrapper;

/**
 * @author jakubchalupa
 * @since 25.02.17
 */
public class RouteWrapper extends AbstractWrapper<String> {

    private String shortName;

    private String longName;

    private Integer typeCode;

    private String color;

    private String agencyId;

    private String agencyName;

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

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
}