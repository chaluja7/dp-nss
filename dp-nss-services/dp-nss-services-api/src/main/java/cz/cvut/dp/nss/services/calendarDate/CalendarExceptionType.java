package cz.cvut.dp.nss.services.calendarDate;

import cz.cvut.dp.nss.services.common.DomainCode;

/**
 * Typ vyjimky CalendarDate.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public enum CalendarExceptionType implements DomainCode {

    /**
     * vyjimecne jede
     */
    INCLUDE(1),

    /**
     * vyjimecne nejede
     */
    EXCLUDE(2);

    private final int code;

    CalendarExceptionType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
