package cz.cvut.dp.nss.services.person;

import cz.cvut.dp.nss.services.common.AbstractGeneratedIdEntity;
import cz.cvut.dp.nss.services.role.Role;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * System user.
 *
 * Protoze je zde natvrdo nastavene schema, tak se VZDY bude cist z tohoto schematu, bez ohledu na pripadne nastaveni
 * v SchemaThreadLocal.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "persons", schema = "global",
    indexes = {@Index(name = "username_index", columnList = "username"), @Index(name = "token_index", columnList = "token")})
public class Person extends AbstractGeneratedIdEntity {

    @Column(unique = true)
    @NotBlank
    private String username;

    @Column
    private String password;

    @Column
    private boolean passwordChangeRequired;

    @Column(unique = true)
    @Size(max = 255)
    private String token;

    @Column
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime tokenValidity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "global.person_role", joinColumns = @JoinColumn(name = "person_id", nullable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false, updatable = false))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "global.person_time_table", joinColumns = @JoinColumn(name = "person_id", nullable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = "time_table_id", nullable = false, updatable = false))
    private Set<TimeTable> timeTables;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Role> getRoles() {
        if(roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<TimeTable> getTimeTables() {
        if(timeTables == null) {
            timeTables = new HashSet<>();
        }
        return timeTables;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public boolean hasRole(Role.Type roleNeeded) {
        return getRoles().contains(new Role(roleNeeded));
    }

    public boolean ownTimeTable(String timeTableId) {
        return hasRole(Role.Type.ADMIN) || getTimeTables().contains(new TimeTable(timeTableId));
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public LocalDateTime getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(LocalDateTime tokenValidity) {
        this.tokenValidity = tokenValidity;
    }
}
