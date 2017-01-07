package cz.cvut.dp.nss.services.common;

import javax.persistence.*;

/**
 * ID se vygeneruje na databazi, persist se musi volat s ID = null.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@MappedSuperclass
public abstract class AbstractGeneratedIdEntity extends AbstractEntity<Long> {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
