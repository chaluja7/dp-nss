package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.exception.BadCredentialsException;
import cz.cvut.dp.nss.exception.PasswordsDoNotMatchException;
import cz.cvut.dp.nss.exception.WeakPasswordException;
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

    /**
     * find person by token
     * @param token api token
     * @return person with given token
     */
    Person getByToken(String token);

    /**
     * prihlasi uzivatele (vygeneruje mu api token a ten se pak posle zpet uzivateli)
     * @param username username
     * @param password password
     * @return vygenerovany api token
     */
    Person generateTokenAndGet(String username, String password) throws BadCredentialsException;

    /**
     * smaze token z atributu z databaze (takze uz se s nim nebude dat prihlasovat)
     * @param token api token
     */
    void destroyToken(String token);

    /**
     * zmeni heslo uzivatele
     * @param personId id uzivatele
     * @param oldPassword stare heslo uzivatele
     * @param newPassword nove heslo uzivatele
     * @param newPasswordConfirmation potvrzeni noveho hesla uzivatele
     */
    void changePassword(Long personId, String oldPassword, String newPassword, String newPasswordConfirmation) throws BadCredentialsException, PasswordsDoNotMatchException, WeakPasswordException;

}
