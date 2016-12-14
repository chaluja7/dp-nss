package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.services.common.EntityService;

/**
 * Common interface for all PersonService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface PersonService extends EntityService<Person, Long> {

    /**
     * find person by username
     * @param username username of person
     * @return person with given username
     */
    Person getByUsername(String username);

}
