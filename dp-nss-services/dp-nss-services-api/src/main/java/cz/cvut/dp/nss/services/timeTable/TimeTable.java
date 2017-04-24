package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.services.common.AbstractAssignedIdEntity;
import cz.cvut.dp.nss.services.person.Person;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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

    /**
     * pocet hodin, v ramci kterych se bude nejdele vyhledavat v tomto jizdnim radu
     * pokud se tedy nenajdou vysledky v case, ktery prekracuje departure + maxTraveTime hodin tak se vyledavani ukonci
     */
    @Column(nullable = false)
    @Min(1)
    @Max(23)
    private Integer maxTravelTime;

    @Column
    private boolean valid;

    /**
     * pokud true, tak se zrovna nahrava novy jizdni rad (z gtfs importniho souboru)
     */
    @Column
    private boolean synchronizing;

    /**
     * pokud nedobehne synchronizace spravne, tak zde bude hlaska :)
     */
    @Column
    @Size(max = 4096)
    private String synchronizingFailMessage;

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

    public boolean isSynchronizing() {
        return synchronizing;
    }

    public String getSynchronizingFailMessage() {
        return synchronizingFailMessage;
    }

    public void setSynchronizingFailMessage(String synchronizingFailMessage) {
        this.synchronizingFailMessage = synchronizingFailMessage;
    }

    public void setSynchronizing(boolean synchronizing) {
        this.synchronizing = synchronizing;
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

    public Integer getMaxTravelTime() {
        return maxTravelTime;
    }

    public void setMaxTravelTime(Integer maxTravelTime) {
        this.maxTravelTime = maxTravelTime;
    }
}
