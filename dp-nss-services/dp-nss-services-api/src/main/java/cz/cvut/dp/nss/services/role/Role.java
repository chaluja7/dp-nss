package cz.cvut.dp.nss.services.role;

import cz.cvut.dp.nss.services.person.Person;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
@Entity
@Table(name = "roles", schema = "global")
public class Role implements Serializable {

    private static final long serialVersionUID = -3457743788806834700L;

    public Role() {
        //empty
    }

    public Role(Type type) {
        this.type = type;
    }

    @Id
    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<Person> persons;

    public Type getType() {
        return type;
    }

    public void getType(Type type) {
        this.type = type;
    }

    public Set<Person> getPersons() {
        if(persons == null) {
            persons = new HashSet<>();
        }
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if(getType() == null || o == null) {
            return false;
        }

        if(!(o instanceof Role)) {
            return false;
        }

        return getType().equals(((Role) o).type);
    }

    @Override
    public int hashCode() {
        return type.toString().hashCode();
    }

    public enum Type {
        ADMIN,
        USER
    }

}
