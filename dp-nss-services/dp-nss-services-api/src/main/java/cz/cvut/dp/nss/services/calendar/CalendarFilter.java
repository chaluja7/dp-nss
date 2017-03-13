package cz.cvut.dp.nss.services.calendar;

import cz.cvut.dp.nss.services.common.AbstractEntityFilter;

import java.time.LocalDate;

/**
 * @author jakubchalupa
 * @since 13.03.17
 */
public class CalendarFilter extends AbstractEntityFilter<String> {

    private LocalDate startDate;

    private LocalDate endDate;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
