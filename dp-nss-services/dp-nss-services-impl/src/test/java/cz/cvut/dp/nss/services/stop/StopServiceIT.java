package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    public static Stop getStop(final String id, String name, Double lat, Double lon) {
        Stop stop = new Stop();
        stop.setId(id);
        stop.setName(name);
        stop.setLat(lat);
        stop.setLon(lon);

        return stop;
    }

}
