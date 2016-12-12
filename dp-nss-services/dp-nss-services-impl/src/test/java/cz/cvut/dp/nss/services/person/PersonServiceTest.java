package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.services.AbstractServiceTest;
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
        Person person = personService.get(1);
        int i = 0;
    }

}
