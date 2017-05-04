package cz.cvut.dp.nss.wrapper.input;

/**
 * Objekt prihlaseni uzivatele.
 *
 * @author jakubchalupa
 * @since 08.03.17
 */
public class LoginWrapper {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
