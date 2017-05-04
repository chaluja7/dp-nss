package cz.cvut.dp.nss.wrapper.output.agency;

import cz.cvut.dp.nss.wrapper.output.common.AbstractWrapper;

/**
 * Objekt dopravce.
 *
 * @author jakubchalupa
 * @since 12.03.17
 */
public class AgencyWrapper extends AbstractWrapper<String> {

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
