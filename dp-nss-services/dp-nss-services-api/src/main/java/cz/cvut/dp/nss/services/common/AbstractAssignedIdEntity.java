package cz.cvut.dp.nss.services.common;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * ID se entite prirazuji "rucne" tedy pred zavolanim persist() musi mit ID prirazene a je za nej zodpovedny uzivatel
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@MappedSuperclass
public abstract class AbstractAssignedIdEntity extends AbstractEntity<String> {

    private static final long serialVersionUID = 1127984985710425894L;

    @Id
    @Column
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
