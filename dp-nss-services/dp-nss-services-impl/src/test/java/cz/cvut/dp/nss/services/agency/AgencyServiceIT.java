package cz.cvut.dp.nss.services.agency;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class AgencyServiceIT extends AbstractServiceIT {

    @Autowired
    private AgencyService agencyService;

    @Test
    public void testCRUD() {
        final String id = "agency" + System.currentTimeMillis();
        String name = "agencyName";
        String phone = "123456789";
        String url = "www.neco.cz";

        Agency agency = getAgency(id, name, phone, url);

        //insert
        agencyService.create(agency);

        //retrieve
        Agency retrieved = agencyService.get(agency.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(id, retrieved.getId());
        Assert.assertEquals(name, retrieved.getName());
        Assert.assertEquals(phone, retrieved.getPhone());
        Assert.assertEquals(url, retrieved.getUrl());

        //update
        name = "newAgencyName";
        retrieved.setName(name);
        agencyService.update(retrieved);

        //check
        retrieved = agencyService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(name, retrieved.getName());

        //delete
        agencyService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(agencyService.get(retrieved.getId()));
    }

    public static Agency getAgency(final String id, String name, String phone, String url) {
        Agency agency = new Agency();
        agency.setId(id);
        agency.setName(name);
        agency.setPhone(phone);
        agency.setUrl(url);

        return agency;
    }

}
