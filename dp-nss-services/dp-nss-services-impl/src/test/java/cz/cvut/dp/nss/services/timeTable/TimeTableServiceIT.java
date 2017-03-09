package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import cz.cvut.dp.nss.services.person.PersonService;
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

    @Autowired
    private PersonService personService;

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

    @Test
    public void testGetAllForPerson() {
        List<TimeTable> all = timeTableService.getAllForPerson(personService.get(1L));
        Assert.assertNotNull(all);
        Assert.assertFalse(all.isEmpty());

        all = timeTableService.getAllForPerson(personService.get(2L));
        Assert.assertNotNull(all);
        Assert.assertFalse(all.isEmpty());
    }

}
