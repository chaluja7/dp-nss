package cz.cvut.dp.nss.services.calendarDate;

/**
 * Typ vyjimky CalendarDate.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public enum CalendarExceptionType {

    /**
     * vyjimecne jede
     */
    INCLUDE(1),

    /**
     * vyjimecne nejede
     */
    EXCLUDE(2);

    private final int typeCode;

    CalendarExceptionType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

}
