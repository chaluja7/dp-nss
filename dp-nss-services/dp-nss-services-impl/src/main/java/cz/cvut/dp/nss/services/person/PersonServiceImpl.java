package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.exception.BadCredentialsException;
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

    @Autowired
    public PersonServiceImpl(PersonDao dao, PasswordEncoder passwordEncoder) {
        super(dao);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional("transactionManager")
    public void create(Person person) {
        if(person == null) return;
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

}
