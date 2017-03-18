package cz.cvut.dp.nss.services.common;

/**
 * @author jakubchalupa
 * @since 08.01.17
 */
public final class EnumUtils {

    /**
     * nalezne enum podle jeho kodu a tridy enumu
     *
     * @param code         kod enumu (hodnota, kterou vraci getCode() na DomainCode)
     * @param enumClass    trida enumu ve ktere hledat kod
     * @return enum, ktery ma pozadovany kod
     * @throws IllegalArgumentException pokud pro danou tridu neni enum s pozadovanym kodem nalezeny
     */
    public static <T extends DomainCode> T fromCode(int code, Class<T> enumClass) {
        for(T enumm : enumClass.getEnumConstants()){
            if(enumm.getCode() == code){
                return enumm;
            }
        }

        throw  new IllegalArgumentException("code not defined for enum " + enumClass.getSimpleName() + ": " + code);
    }

    /**
     * nalezne enum podle jeho jmena a tridy enumu
     *
     * @param name         name enumu (hodnota, kterou vraci name() na DomainCode)
     * @param enumClass    trida enumu ve ktere hledat kod
     * @return enum, ktery ma pozadovany kod
     * @throws IllegalArgumentException pokud pro danou tridu neni enum s pozadovanym kodem nalezeny
     */
    public static <T extends DomainCode> T fromName(String name, Class<T> enumClass) {
        for(T enumm : enumClass.getEnumConstants()){
            if(enumm.name().equalsIgnoreCase(name)){
                return enumm;
            }
        }

        throw  new IllegalArgumentException("name not defined for enum " + enumClass.getSimpleName() + ": " + name);
    }

}
