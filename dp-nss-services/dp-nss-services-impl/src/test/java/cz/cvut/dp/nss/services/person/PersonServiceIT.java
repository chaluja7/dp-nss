package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.exception.BadCredentialsException;
import cz.cvut.dp.nss.services.AbstractServiceIT;
import cz.cvut.dp.nss.services.role.Role;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 12.12.16
 */
public class PersonServiceIT extends AbstractServiceIT {

    @Autowired
    private PersonService personService;

    private static final String USER = "user";

    private static final String PASSWORD = "admin";

    private static final String TOKEN = "token";

    private long currentPersonId;

    @Before
    public void init() {
        Person person = new Person();
        person.setUsername(USER);
        person.setPassword(PASSWORD);
        person.setToken(TOKEN);

        personService.create(person);
        currentPersonId = person.getId();
    }

    @After
    public void destroy() {
        personService.delete(currentPersonId);
    }

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


        //roles
        Assert.assertTrue(retrieved.getRoles().isEmpty());
        retrieved.getRoles().add(new Role(Role.Type.ADMIN));
        personService.update(retrieved);

        retrieved = personService.get(retrieved.getId());
        Assert.assertNotNull(retrieved.getRoles());
        Assert.assertTrue(retrieved.hasRole(Role.Type.ADMIN));


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

    @Test
    public void testGetByToken() {
        Person person = personService.getByToken(TOKEN);
        Assert.assertNotNull(person);
    }

    @Test(expected = BadCredentialsException.class)
    public void testGenerateTokenInvalid() throws BadCredentialsException {
        personService.generateTokenAndGet("alksdf", "asdf");
    }

    @Test
    public void testGenerateTokenValid() throws BadCredentialsException {
        Person person = personService.generateTokenAndGet(USER, PASSWORD);
        Assert.assertNotNull(person.getToken());
        Assert.assertTrue(person.getToken().length() > 10);

        Person byToken = personService.getByToken(person.getToken());
        Assert.assertNotNull(byToken);
        Assert.assertEquals(person.getToken(), byToken.getToken());
    }

    @Test
    public void testDestroyToken() {
        Person person = personService.getByToken(TOKEN);
        Assert.assertNotNull(person);

        personService.destroyToken(TOKEN);

        Person byUsername = personService.getByUsername(USER);
        Assert.assertNotNull(byUsername);
        Assert.assertNull(byUsername.getToken());

        byUsername.setToken(TOKEN);
        personService.update(byUsername);
    }

    public static Person getPerson(String username, String password) {
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);

        return person;
    }


}
