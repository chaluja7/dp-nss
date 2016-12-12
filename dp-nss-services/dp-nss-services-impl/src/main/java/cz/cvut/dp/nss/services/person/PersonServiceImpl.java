package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.persistence.person.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of PersonService.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    protected PersonDao personDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person get(long id) {
        return personDao.find(id);
    }

    @Override
    @Transactional
    //@PreAuthorize("hasRole('ROLE_USER')")
    public void update(Person person) {
        personDao.update(person);
    }

    @Override
    @Transactional
    //@PreAuthorize("hasRole('ROLE_USER')")
    public void create(Person person) {
        personDao.create(person);
    }

    @Override
    @Transactional
    //@PreAuthorize("hasRole('ROLE_USER')")
    public void delete(long id) {
        personDao.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> getAll() {
        return personDao.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person getByUsername(String username) {
        return personDao.getPersonByUsername(username);
    }

}
