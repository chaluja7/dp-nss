package cz.cvut.dp.nss.controller.login;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.person.AdminPersonController;
import cz.cvut.dp.nss.controller.interceptor.SecurityInterceptor;
import cz.cvut.dp.nss.exception.BadCredentialsException;
import cz.cvut.dp.nss.exception.ForbiddenException;
import cz.cvut.dp.nss.services.person.Person;
import cz.cvut.dp.nss.services.person.PersonService;
import cz.cvut.dp.nss.wrapper.input.LoginWrapper;
import cz.cvut.dp.nss.wrapper.output.person.PersonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller obsluhujici prihlaseni a odhlaseni.
 *
 * @author jakubchalupa
 * @since 06.03.17
 */
@RestController
public class LoginController extends AbstractController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public PersonWrapper loginUser(@RequestBody LoginWrapper loginWrapper) throws ForbiddenException {
        final Person person;
        try {
            person = personService.generateTokenAndGet(loginWrapper.getUsername(), loginWrapper.getPassword());
        } catch (BadCredentialsException e) {
            throw new ForbiddenException();
        }

        return AdminPersonController.getPersonWrapper(person);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logoutUser(@RequestHeader(SecurityInterceptor.SECURITY_HEADER) String userToken) {
        //je mozne zavolat i s nevalidnim tokenem, v tom pripade se vrati 200, aby to bylo idempotentni
        personService.destroyToken(userToken);
    }

}
