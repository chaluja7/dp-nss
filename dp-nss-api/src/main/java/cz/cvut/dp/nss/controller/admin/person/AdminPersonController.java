package cz.cvut.dp.nss.controller.admin.person;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.interceptor.CheckAccess;
import cz.cvut.dp.nss.controller.interceptor.SecurityInterceptor;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.exception.UnauthorizedException;
import cz.cvut.dp.nss.services.person.Person;
import cz.cvut.dp.nss.services.person.PersonService;
import cz.cvut.dp.nss.services.role.Role;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.wrapper.input.ResetPasswordWrapper;
import cz.cvut.dp.nss.wrapper.output.person.PersonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jakubchalupa
 * @since 23.04.17
 */
@RestController
@RequestMapping(value = "/admin/person")
public class AdminPersonController extends AdminAbstractController {

    @Autowired
    private PersonService personService;

    @CheckAccess(Role.Type.ADMIN)
    @RequestMapping(method = RequestMethod.GET)
    public List<PersonWrapper> getPersons() {
        List<Person> persons = personService.getAll();
        List<PersonWrapper> wrappers = new ArrayList<>();

        for(Person person : persons) {
            wrappers.add(getPersonWrapper(person));
        }

        return wrappers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PersonWrapper get(@PathVariable("id") Long id, @RequestHeader(SecurityInterceptor.SECURITY_HEADER) String xAuth) throws BadRequestException, UnauthorizedException {
        Person person = personService.get(id);
        if(person == null) throw new ResourceNotFoundException();
        //uzivatel muze vylistovat jen sam sebe, pokud tedy neni admin
        if(person.getToken() == null || !person.getToken().equals(xAuth)) {
            Person currentPerson = personService.getByToken(xAuth);
            if(!currentPerson.hasRole(Role.Type.ADMIN)) {
                throw new UnauthorizedException();
            }
        }

        return getPersonWrapper(person);
    }

    //pouze update jizdnich radu
    @CheckAccess(Role.Type.ADMIN)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PersonWrapper update(@PathVariable("id") Long id, @RequestBody PersonWrapper wrapper) throws BadRequestException, UnauthorizedException {
        Person person = personService.get(id);
        if(person == null) throw new ResourceNotFoundException();

        Set<String> timeTables = wrapper.getTimeTables();
        if(timeTables == null) timeTables = new HashSet<>();

        personService.updateTimeTables(person.getId(), timeTables);
        return getPersonWrapper(personService.get(person.getId()));
    }

    @RequestMapping(value = "/{id}/resetPassword", method = RequestMethod.PUT)
    public void changePassword(@PathVariable("id") Long id, @RequestBody ResetPasswordWrapper wrapper, @RequestHeader(SecurityInterceptor.SECURITY_HEADER) String xAuth) throws BadRequestException, UnauthorizedException {
        Person person = personService.get(id);
        if(person == null) throw new ResourceNotFoundException();
        //uzivatel muze zmenit heslo jen sam sobe
        if(!person.getToken().equals(xAuth)) throw new UnauthorizedException();
        if(wrapper.getOldPassword() == null || wrapper.getNewPassword() == null || wrapper.getNewPasswordConfirmation() == null) throw new BadRequestException();

        try {
            personService.changePassword(id, wrapper.getOldPassword(), wrapper.getNewPassword(), wrapper.getNewPasswordConfirmation());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public static PersonWrapper getPersonWrapper(Person person) {
        if(person == null) return null;

        PersonWrapper wrapper = new PersonWrapper();
        wrapper.setId(person.getId());
        wrapper.setUsername(person.getUsername());
        wrapper.setToken(person.getToken());

        Set<String> roles = new HashSet<>();
        for(Role role : person.getRoles()) {
            roles.add(role.getType().name());
        }
        wrapper.setRoles(roles);

        Set<String> timeTables = new HashSet<>();
        for(TimeTable timeTable : person.getTimeTables()) {
            timeTables.add(timeTable.getId());
        }
        wrapper.setTimeTables(timeTables);

        return wrapper;
    }

}
