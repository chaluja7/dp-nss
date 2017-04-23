package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.exception.BadCredentialsException;
import cz.cvut.dp.nss.exception.PasswordsDoNotMatchException;
import cz.cvut.dp.nss.exception.WeakPasswordException;
import cz.cvut.dp.nss.persistence.person.PersonDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of PersonService.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Service
public class PersonServiceImpl extends AbstractEntityService<Person, Long, PersonDao> implements PersonService {

    private PasswordEncoder passwordEncoder;

    private static final int MIN_PASSWORD_LENGTH = 8;

    @Autowired
    public PersonServiceImpl(PersonDao dao, PasswordEncoder passwordEncoder) {
        super(dao);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional("transactionManager")
    public void create(Person person) {
        if(person == null) return;
        //zde schvalne neni kontrola na delku hesla, protoze pri vkladani se insertuje pouze jednorazove heslo
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        dao.create(person);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public Person getByUsername(String username) {
        return dao.getPersonByUsername(username);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public Person getByToken(String token) {
        return token != null ? dao.getByToken(token) : null;
    }

    @Override
    @Transactional(value = "transactionManager")
    public Person generateTokenAndGet(String username, String password) throws BadCredentialsException {
        Person person = dao.getPersonByUsername(username);
        if(person == null || person.getPassword() == null) throw new BadCredentialsException();

        //heslo musim zkontrolovat az nyni, protoze hash je pokazde jinaci
        if(!passwordEncoder.matches(password, person.getPassword())) throw new BadCredentialsException();

        final String token = UUID.randomUUID().toString().replaceAll("-", "");
        person.setToken(token);
        dao.update(person);

        return person;
    }

    @Override
    @Transactional(value = "transactionManager")
    public void destroyToken(String token) {
        Person person = getByToken(token);
        if(person != null) {
            person.setToken(null);
            dao.update(person);
        }
    }

    @Override
    @Transactional(value = "transactionManager")
    public void changePassword(Long personId, String oldPassword, String newPassword, String newPasswordConfirmation) throws BadCredentialsException, PasswordsDoNotMatchException, WeakPasswordException {
        Person person = dao.find(personId);
        if(person == null) throw new BadCredentialsException();

        //heslo musim zkontrolovat az nyni, protoze hash je pokazde jinaci
        if(!passwordEncoder.matches(oldPassword, person.getPassword())) throw new BadCredentialsException();
        if(newPassword == null || newPassword.length() < MIN_PASSWORD_LENGTH) throw new WeakPasswordException();
        if(!newPassword.equals(newPasswordConfirmation)) throw new PasswordsDoNotMatchException();

        //ok muzu zmenit
        person.setPassword(passwordEncoder.encode(newPassword));
        dao.update(person);
    }

}
