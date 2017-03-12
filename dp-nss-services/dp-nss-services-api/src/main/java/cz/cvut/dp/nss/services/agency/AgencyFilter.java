package cz.cvut.dp.nss.services.agency;

import cz.cvut.dp.nss.services.common.AbstractEntityFilter;

/**
 * @author jakubchalupa
 * @since 12.03.17
 */
public class AgencyFilter extends AbstractEntityFilter<String> {

    private String name;

    private String url;

    private String phone;

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

}
