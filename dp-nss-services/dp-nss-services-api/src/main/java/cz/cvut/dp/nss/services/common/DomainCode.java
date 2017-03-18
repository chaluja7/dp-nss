package cz.cvut.dp.nss.services.common;

/**
 * Spolecny interface pro enumy entit. Zejma kvuli mapovani name() na id enumu v GTFS formatu dat.
 *
 * @author jakubchalupa
 * @since 08.01.17
 */
public interface DomainCode {

    int getCode();

    String name();

}
