package cz.cvut.dp.nss.graph.services.calendarDate;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNode;
import cz.cvut.dp.nss.graph.services.common.AbstractNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * CREATE CONSTRAINT ON (n:CalendarDateNode) ASSERT n.calendarDateId IS UNIQUE
 *
 * @author jakubchalupa
 * @since 11.02.17
 */
@NodeEntity
public class CalendarDateNode extends AbstractNode {

    @Property
    private Long calendarDateId;

    /**
     * presny cas pulnoci prislusneho dne ve vterinach since epoch
     */
    @Property
    private Long dateInSeconds;

    /**
     * true = include, false = exclude
     */
    @Property
    private Boolean include;

    /**
     * Calendar tohoto date
     */
    @Relationship(type = "IN_CALENDAR", direction = Relationship.OUTGOING)
    private CalendarNode calendarNode;

    public Long getCalendarDateId() {
        return calendarDateId;
    }

    public void setCalendarDateId(Long calendarDateId) {
        this.calendarDateId = calendarDateId;
    }

    public Long getDateInSeconds() {
        return dateInSeconds;
    }

    public void setDateInSeconds(Long dateInSeconds) {
        this.dateInSeconds = dateInSeconds;
    }

    public Boolean getInclude() {
        return include;
    }

    public void setInclude(Boolean include) {
        this.include = include;
    }

    public CalendarNode getCalendarNode() {
        return calendarNode;
    }

    public void setCalendarNode(CalendarNode calendarNode) {
        this.calendarNode = calendarNode;
    }
}
