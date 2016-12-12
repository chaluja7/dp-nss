package cz.cvut.dp.nss.services.station;

import java.util.List;

/**
 * Common interface for all StationService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface StationService {

    /**
     * find station by id
     * @param id id of a station
     * @return station by id or null
     */
    Station getStation(long id);

    /**
     * update station
     * @param station station to update
     * @return updated station
     */
    Station updateStation(Station station);

    /**
     * persists station
     * @param station station to persist
     */
    void createStation(Station station);

    /**
     * delete station
     * @param id id of station to delete
     */
    void deleteStation(long id);

    /**
     * find all stations
     * @return all stations
     */
    List<Station> getAll();

    /**
     * find all stations with pattern in name
     * @param pattern pattern of station name
     * @return all stations with pattern
     */
    List<Station> getAllByNamePattern(String pattern);

    /**
     * find station by its title
     * @param title title of station
     * @return station with given title or null
     */
    Station getStationByTitle(String title);

    /**
     * find station by its name
     * @param name name of station
     * @return station with given name or null
     */
    Station getStationByName(String name);

    /**
     * get count of all stations
     * @return count of all stations
     */
    int getCountAll();

    /**
     * find all station ordered by orderColumn
     * @param orderColumn column to order results
     * @return ordered list of stations
     */
    List<Station> getAllWithOrder(String orderColumn);

}
