package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class StopServiceIT extends AbstractServiceIT {

    @Autowired
    private StopService stopService;

    @Test
    public void testCRUD() {
        final String id = "stop" + System.currentTimeMillis();
        String name = "stopName";
        Double lat = 40.0;
        Double lon = 50.0;

        Stop stop = getStop(id, name, lat, lon);

        //insert
        stopService.create(stop);

        //retrieve
        Stop retrieved = stopService.get(stop.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(id, retrieved.getId());
        Assert.assertEquals(name, retrieved.getName());
        Assert.assertEquals(lat, retrieved.getLat());
        Assert.assertEquals(lon, retrieved.getLon());

        //update
        name = "newStopName";
        retrieved.setName(name);
        stopService.update(retrieved);

        //check
        retrieved = stopService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(name, retrieved.getName());

        //delete
        stopService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(stopService.get(retrieved.getId()));
    }

    @Test
    public void testGetAllInRange() {
        List<Stop> allInRange = stopService.getAllInRange(0, 1000);
        Assert.assertNotNull(allInRange);
    }

    @Test
    public void testGetByFilter() {
        List<Stop> byFilter = stopService.getByFilter(getFilter(),0, 15, "parentStopId", true);
        Assert.assertNotNull(byFilter);
    }

    @Test
    public void testGetCountByFilter() {
        long countByFilter = stopService.getCountByFilter(getFilter());
        Assert.assertTrue(countByFilter >= 0);
    }

    @Test
    public void testIteratorOverAllStops() {
        Iterator<Stop> stopIterator = stopService.iteratorOverAllStops();
        Set<String> stopIds = new HashSet<>();
        while(stopIterator.hasNext()) {
            Stop next = stopIterator.next();
            Assert.assertNotNull(next);

            stopIds.add(next.getId());
        }

        Assert.assertNotNull(stopIds);
    }

    @Test
    public void testFindStopNamesByStartPattern() {
        Set<String> set = stopService.findStopNamesByStartPattern("flo");
        Assert.assertNotNull(set);
    }

    @Test
    public void testFindStopsBySearchQuery() {
        List<Stop> list = stopService.findStopsBySearchQuery("U10");
        Assert.assertNotNull(list);
    }

    public static Stop getStop(final String id, String name, Double lat, Double lon) {
        Stop stop = new Stop();
        stop.setId(id);
        stop.setName(name);
        stop.setLat(lat);
        stop.setLon(lon);

        return stop;
    }

    public static StopFilter getFilter() {
        StopFilter filter = new StopFilter();
        filter.setName("eli");

        return filter;
    }

}
