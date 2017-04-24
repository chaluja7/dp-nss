package cz.cvut.dp.nss.persistence.person;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.person.Person;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * JPA implementation of PersonDao.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class PersonDao extends AbstractGenericJpaDao<Person, Long> {

    public PersonDao() {
        super(Person.class);
    }

    public Person getPersonByUsername(String username) {
        Query<Person> query = sessionFactory.getCurrentSession().createQuery("select p from Person p where username = :username", Person.class);
        query.setParameter("username", username);

        return query.uniqueResult();
    }

    public Person getByToken(String token) {
        final String queryString = "select p from Person p where token = :token and tokenValidity >= :validity";
        Query<Person> query = sessionFactory.getCurrentSession().createQuery(queryString, Person.class);
        query.setParameter("token", token);
        query.setParameter("validity", LocalDateTime.now());

        return query.uniqueResult();
    }

    @Override
    public void truncateAll() {
        throw new UnsupportedOperationException();
    }
}
