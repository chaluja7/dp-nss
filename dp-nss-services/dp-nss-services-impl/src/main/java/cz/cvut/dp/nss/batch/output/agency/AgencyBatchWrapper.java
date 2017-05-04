package cz.cvut.dp.nss.batch.output.agency;

/**
 * Wrapper dopravce pro export.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class AgencyBatchWrapper {

    private String id;

    private String name;

    private String url;

    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
