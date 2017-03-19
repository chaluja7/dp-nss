package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.services.common.EntityService;
import cz.cvut.dp.nss.services.person.Person;

import java.util.List;

/**
 * Common interface for all TimeTableService implementations.
 *
 * @author jakubchalupa
 * @since 02.03.17
 */
public interface TimeTableService extends EntityService<TimeTable, String> {

    /**
     * find all timetables
     * @param validOnly if select only valid
     * @return all timetables
     */
    List<TimeTable> getAll(boolean validOnly);

    List<TimeTable> getAllForPerson(Person person);

    void generateTimeTableToDatabases(String schema, String folder) throws Throwable;

}
