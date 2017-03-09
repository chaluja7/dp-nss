package cz.cvut.dp.nss.controller.login;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.interceptor.SecurityInterceptor;
import cz.cvut.dp.nss.exception.BadCredentialsException;
import cz.cvut.dp.nss.exception.UnauthorizedException;
import cz.cvut.dp.nss.services.person.Person;
import cz.cvut.dp.nss.services.person.PersonService;
import cz.cvut.dp.nss.services.role.Role;
import cz.cvut.dp.nss.wrapper.in.LoginWrapper;
import cz.cvut.dp.nss.wrapper.out.person.PersonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
@RestController
public class LoginController extends AbstractController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public PersonWrapper loginUser(@RequestBody LoginWrapper loginWrapper) throws UnauthorizedException {
        final Person person;
        try {
            person = personService.generateTokenAndGet(loginWrapper.getUsername(), loginWrapper.getPassword());
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException();
        }

        return getPersonWrapper(person);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logoutUser(@RequestHeader(SecurityInterceptor.SECURITY_HEADER) String userToken) {
        //je mozne zavolat i s nevalidnim tokenem, v tom pripade se vrati 200, aby to bylo idempotentni
        personService.destroyToken(userToken);
    }

    private static PersonWrapper getPersonWrapper(Person person) {
        if(person == null) return null;

        PersonWrapper wrapper = new PersonWrapper();
        wrapper.setUsername(person.getUsername());
        wrapper.setToken(person.getToken());

        Set<String> roles = new HashSet<>();
        for(Role role : person.getRoles()) {
            roles.add(role.getType().name());
        }
        wrapper.setRoles(roles);

        return wrapper;
    }

}
