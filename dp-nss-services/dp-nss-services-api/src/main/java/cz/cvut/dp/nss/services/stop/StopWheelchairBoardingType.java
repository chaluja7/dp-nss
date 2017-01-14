package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.common.DomainCode;

/**
 * Typ dostupnosti stanice pro vozickare
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public enum StopWheelchairBoardingType implements DomainCode {

    /**
     * info neni dostupne
     */
    NO_INFO(0),

    /**
     * dostupne pro vozickare
     */
    BOARDING_POSSIBLE(1),

    /**
     * nedostupne pro vozickare
     */
    BOARDING_IMPOSSIBLE(2);

    private final int code;

    StopWheelchairBoardingType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
