package cz.cvut.dp.nss.services.person;

import java.util.List;

/**
 * Common interface for all PersonService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface PersonService {

    /**
     * find person by id
     * @param id id of a person
     * @return person by id or null
     */
    Person get(long id);

    /**
     * update person
     * @param person person to update
     */
    void update(Person person);

    /**
     * persists person
     * @param person person to persist
     */
    void create(Person person);

    /**
     * delete person
     * @param id id of person to delete
     */
    void delete(long id);

    /**
     * find all persons
     * @return all persons
     */
    List<Person> getAll();


    /**
     * find person by username
     * @param username username of person
     * @return person with given username
     */
    Person getByUsername(String username);

}
