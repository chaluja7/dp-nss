package cz.cvut.dp.nss.services.region;

import java.util.List;

/**
 * Common interface for all RegionService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface RegionService {

    /**
     * find region by id
     * @param id id of a region
     * @return region by id or null
     */
    Region getRegion(long id);

    /**
     * update region
     * @param region region to update
     * @return updated region
     */
    Region updateRegion(Region region);

    /**
     * persists region
     * @param region region to persist
     */
    void createRegion(Region region);

    /**
     * delete region
     * @param id id of region to delete
     */
    void deleteRegion(long id);

    /**
     * find all regions
     * @return all regions
     */
    List<Region> getAll();

}
