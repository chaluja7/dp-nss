package cz.cvut.dp.nss.graph.services.calendar;

import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNode;
import cz.cvut.dp.nss.graph.services.common.AbstractNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * CREATE CONSTRAINT ON (n:CalendarNode) ASSERT n.calendarId IS UNIQUE
 *
 * @author jakubchalupa
 * @since 11.02.17
 */
@NodeEntity
public class CalendarNode extends AbstractNode {

    @Property
    private String calendarId;

    @Property
    private Boolean monday;

    @Property
    private Boolean tuesday;

    @Property
    private Boolean wednesday;

    @Property
    private Boolean thursday;

    @Property
    private Boolean friday;

    @Property
    private Boolean saturday;

    @Property
    private Boolean sunday;

    @Property
    private Long fromDateInSeconds;

    @Property
    private Long toDateInSeconds;

    /**
     * navazana dny vyjimek
     */
    @Relationship(type = "IN_CALENDAR", direction = Relationship.INCOMING)
    private List<CalendarDateNode> calendarDateNodes;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Boolean getMonday() {
        return monday;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getFriday() {
        return friday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Long getFromDateInSeconds() {
        return fromDateInSeconds;
    }

    public void setFromDateInSeconds(Long fromDateInSeconds) {
        this.fromDateInSeconds = fromDateInSeconds;
    }

    public Long getToDateInSeconds() {
        return toDateInSeconds;
    }

    public void setToDateInSeconds(Long toDateInSeconds) {
        this.toDateInSeconds = toDateInSeconds;
    }

    public List<CalendarDateNode> getCalendarDateNodes() {
        return calendarDateNodes;
    }

    public void setCalendarDateNodes(List<CalendarDateNode> calendarDateNodes) {
        this.calendarDateNodes = calendarDateNodes;
    }
}
