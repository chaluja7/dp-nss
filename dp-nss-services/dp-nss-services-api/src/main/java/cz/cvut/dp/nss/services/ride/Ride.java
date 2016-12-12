package cz.cvut.dp.nss.services.ride;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import cz.cvut.dp.nss.services.line.Line;
import cz.cvut.dp.nss.services.operationInterval.OperationInterval;
import cz.cvut.dp.nss.services.stop.Stop;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * One ride of a Line. (according to GTFS this is TRIP)
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
@Entity
@Table(name = "rides",
    indexes = {@Index(name = "ride_line_index", columnList = "line_id"),
        @Index(name = "ride_operation_interval_index", columnList = "operation_interval_id")})
public class Ride extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "operation_interval_id")
    private OperationInterval operationInterval;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ride")
    @OrderBy("stopOrder ASC")
    private List<Stop> stops;

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public OperationInterval getOperationInterval() {
        return operationInterval;
    }

    public void setOperationInterval(OperationInterval operationInterval) {
        this.operationInterval = operationInterval;
    }

    public List<Stop> getStops() {
        if (stops == null) {
            stops = new ArrayList<>();
        }

        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void addStop(Stop stop) {
        if (!getStops().contains(stop)) {
            stops.add(stop);
        }

        stop.setRide(this);
    }

}
