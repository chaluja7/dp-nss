package cz.cvut.dp.nss.services.region;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import cz.cvut.dp.nss.services.station.Station;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Geographical region.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "regions")
public class Region extends AbstractEntity {

    @Column(unique = true)
    @NotBlank
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    private List<Station> stations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Station> getStations() {
        if(stations == null) {
            stations = new ArrayList<>();
        }

        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

}
