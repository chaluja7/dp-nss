package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.services.common.AbstractAssignedIdEntity;
import cz.cvut.dp.nss.services.person.Person;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Dostupne jizdny rady.
 *
 * Protoze je zde natvrdo nastavene schema, tak se VZDY bude cist z tohoto schematu, bez ohledu na pripadne nastaveni
 * v SchemaThreadLocal.
 *
 * @author jakubchalupa
 * @since 02.03.17
 */
@Entity
@Table(name = "time_tables", schema = "global")
public class TimeTable extends AbstractAssignedIdEntity {

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column
    private boolean valid;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "timeTables")
    private Set<Person> persons;

    public TimeTable() {
        //empty
    }

    public TimeTable(String id) {
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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
}
