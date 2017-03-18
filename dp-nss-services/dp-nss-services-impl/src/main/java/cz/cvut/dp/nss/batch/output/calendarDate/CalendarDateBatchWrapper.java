package cz.cvut.dp.nss.batch.output.calendarDate;

import java.math.BigDecimal;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class CalendarDateBatchWrapper {

    private BigDecimal id;

    private String calendarId;

    private String date;

    private Integer exceptionType;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(Integer exceptionType) {
        this.exceptionType = exceptionType;
    }
}
