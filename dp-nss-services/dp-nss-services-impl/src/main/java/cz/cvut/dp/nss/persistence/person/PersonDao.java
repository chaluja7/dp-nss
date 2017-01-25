package cz.cvut.dp.nss.persistence.person;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.person.Person;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of PersonDao.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Component
public class PersonDao extends AbstractGenericJpaDao<Person, Long> {

    public PersonDao() {
        super(Person.class);
    }

    @SuppressWarnings("JpaQlInspection")
    public Person getPersonByUsername(String username) {
        Query<Person> query = sessionFactory.getCurrentSession().createQuery("select p from Person p where username = :username", Person.class);
        query.setParameter("username", username);

        return query.uniqueResult();
    }
}
