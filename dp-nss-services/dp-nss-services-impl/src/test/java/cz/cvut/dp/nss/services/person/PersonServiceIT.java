package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 12.12.16
 */
public class PersonServiceIT extends AbstractServiceIT {

    @Autowired
    private PersonService personService;

    @Test
    public void testCRUD() {
        String username = "user" + System.currentTimeMillis();
        String password = "password";

        Person person = getPerson(username, password);

        //insert
        personService.create(person);

        //retrieve
        Person retrieved = personService.get(person.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(username, retrieved.getUsername());

        //update
        username = "newUserName";
        retrieved.setUsername(username);
        personService.update(retrieved);

        //check
        retrieved = personService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(username, retrieved.getUsername());

        //delete
        personService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(personService.get(retrieved.getId()));
    }

    @Test
    public void testGet() {
        Person person = personService.get(1L);
        Person person2 = personService.getByUsername("admin");

        Assert.assertNotNull(person);
        Assert.assertNotNull(person2);
    }

    public static Person getPerson(String username, String password) {
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);

        return person;
    }


}
