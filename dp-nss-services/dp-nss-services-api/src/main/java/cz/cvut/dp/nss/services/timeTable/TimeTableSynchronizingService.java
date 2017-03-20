package cz.cvut.dp.nss.services.timeTable;

/**
 * Common interface for all TimeTableSynchronizingService implementations.
 *
 * @author jakubchalupa
 * @since 20.03.17
 */
public interface TimeTableSynchronizingService {

    /**
     * @param schema aktualni schema db
     * @param folder slozka, ve ktere jsou ulozene jizdni rady (.txt soubory)
     * @throws Throwable throwable
     */
    void generateTimeTableToDatabases(String schema, String folder) throws Throwable;

}
