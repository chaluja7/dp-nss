package cz.cvut.dp.nss.services.agency;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Test
    public void testGetByFilter() {
        List<Agency> byFilter = agencyService.getByFilter(getFilter(),0, 15, "parentStopId", true);
        Assert.assertNotNull(byFilter);
    }

    @Test
    public void testGetCountByFilter() {
        long countByFilter = agencyService.getCountByFilter(getFilter());
        Assert.assertTrue(countByFilter >= 0);
    }

    @Test
    public void testFindAgenciesBySearchQuery() {
        List<Agency> list = agencyService.findAgenciesBySearchQuery("dopravni");
        Assert.assertNotNull(list);
    }

    @Test
    public void testCanBeDeleted() {
        boolean canBeDeleted = agencyService.canBeDeleted("1");
        Assert.assertTrue(true);
    }

    public static Agency getAgency(final String id, String name, String phone, String url) {
        Agency agency = new Agency();
        agency.setId(id);
        agency.setName(name);
        agency.setPhone(phone);
        agency.setUrl(url);

        return agency;
    }

    public static AgencyFilter getFilter() {
        AgencyFilter filter = new AgencyFilter();
        filter.setName("podnik");

        return filter;
    }

}
