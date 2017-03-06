package cz.cvut.dp.nss.controller.login;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.exception.BadCredentialsException;
import cz.cvut.dp.nss.exception.UnauthorizedException;
import cz.cvut.dp.nss.services.person.Person;
import cz.cvut.dp.nss.services.person.PersonService;
import cz.cvut.dp.nss.wrapper.out.person.PersonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
@RestController
@RequestMapping(value = "/login")
public class LoginController extends AbstractController {

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.GET)
    public PersonWrapper loginUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password) throws UnauthorizedException {
        final Person person;
        try {
            person = personService.generateTokenAndGet(username, password);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException();
        }

        return getPersonWrapper(person);
    }

    private static PersonWrapper getPersonWrapper(Person person) {
        if(person == null) return null;

        PersonWrapper wrapper = new PersonWrapper();
        wrapper.setUsername(person.getUsername());
        wrapper.setToken(person.getToken());

        return wrapper;
    }

}
