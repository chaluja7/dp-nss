package cz.cvut.dp.nss.wrapper.output.person;

import java.util.Set;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
public class PersonWrapper {

    private Long id;

    private String username;

    private String token;

    private Set<String> roles;

    private Set<String> timeTables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Set<String> timeTables) {
        this.timeTables = timeTables;
    }
}
