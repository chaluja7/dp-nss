package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.services.AbstractServiceTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 12.12.16
 */
public class PersonServiceTest extends AbstractServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    public void testGet() {
        Person person = personService.get(1L);
        Person person2 = personService.getByUsername("admin");

        Assert.assertNotNull(person);
        Assert.assertNotNull(person2);
    }

}
