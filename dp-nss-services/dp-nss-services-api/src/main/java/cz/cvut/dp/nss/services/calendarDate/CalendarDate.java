package cz.cvut.dp.nss.services.calendarDate;

import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.common.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Vyjimky v platnosti tripu dle Calendar.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
@Entity
@Table(name = "calendar_date", indexes = {@Index(name = "calendar_date_calendar_index", columnList = "calendar_id")})
public class CalendarDate extends AbstractEntity<Long> {

    /**
     * datum vyjimky z Calendar
     */
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.LocalDateType")
    @NotNull
    private LocalDate date;

    /**
     * typ vyjimky - bud vyjimecne jede (INCLUDE) nebo nejede (EXCLUDE)
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CalendarExceptionType exceptionType;

    /**
     * zaznam v calendar, ke kteremu patri vyjimka. schvalne je EAGER.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CalendarExceptionType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(CalendarExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

}
