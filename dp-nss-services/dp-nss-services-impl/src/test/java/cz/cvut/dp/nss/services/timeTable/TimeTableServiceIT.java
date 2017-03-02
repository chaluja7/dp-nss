package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 02.03.17
 */
public class TimeTableServiceIT extends AbstractServiceIT {

    @Autowired
    private TimeTableService timeTableService;

    @Test
    public void testGetAllValid() {
        List<TimeTable> allValid = timeTableService.getAll(true);
        Assert.assertNotNull(allValid);
        Assert.assertFalse(allValid.isEmpty());
    }

    @Test
    public void testGetAll() {
        List<TimeTable> all = timeTableService.getAll(false);
        Assert.assertNotNull(all);
        Assert.assertFalse(all.isEmpty());
    }

}
