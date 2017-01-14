package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.common.DomainCode;

/**
 * Typ dostupnosti tripu pro vozickare.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public enum TripWheelchairAccessibleType implements DomainCode {

    /**
     * info neni dostupne
     */
    NO_INFO(0),

    /**
     * muze byt prevezen alespon jeden vozicek
     */
    ACCESSIBLE(1),

    /**
     * vozicek nemuze byt prevezen
     */
    INACCESSIBLE(2);

    private final int code;

    TripWheelchairAccessibleType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
