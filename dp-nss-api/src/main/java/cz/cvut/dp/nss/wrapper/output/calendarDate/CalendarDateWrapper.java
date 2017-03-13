package cz.cvut.dp.nss.wrapper.output.calendarDate;

import cz.cvut.dp.nss.wrapper.output.common.AbstractWrapper;

/**
 * @author jakubchalupa
 * @since 13.03.17
 */
public class CalendarDateWrapper extends AbstractWrapper<Long> {

    private String date;

    private Integer exceptionType;

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
