package cz.cvut.dp.nss.services.agency;

import cz.cvut.dp.nss.services.common.AbstractAssignedIdEntity;
import cz.cvut.dp.nss.services.route.Route;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Dopravni spolecnost provozujici prepravu (napr. CSAD)
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
@Entity
@Table(name = "agencies")
public class Agency extends AbstractAssignedIdEntity {

    /**
     * jmeno, schvalne neni unique dle specifikace
     */
    @Column
    @Size(min = 1, max = 255)
    private String name;

    /**
     * web
     */
    @Column
    @Size(max = 255)
    private String url;

    /**
     * telefon na kontaktni osobu
     */
    @Column
    @Size(max = 255)
    private String phone;

    /**
     * trati provozovane touto spolecnosti
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "agency")
    private List<Route> routes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Route> getRoutes() {
        if(routes == null) {
            routes = new ArrayList<>();
        }

        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route route) {
        if(!getRoutes().contains(route)) {
            routes.add(route);
        }

        route.setAgency(this);
    }

}
