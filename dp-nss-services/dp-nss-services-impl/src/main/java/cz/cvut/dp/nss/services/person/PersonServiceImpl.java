package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.persistence.person.PersonDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PersonService.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Service
public class PersonServiceImpl extends AbstractEntityService<Person, Long, PersonDao> implements PersonService {

    @Autowired
    public PersonServiceImpl(PersonDao dao) {
        super(dao);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person getByUsername(String username) {
        return dao.getPersonByUsername(username);
    }

}
