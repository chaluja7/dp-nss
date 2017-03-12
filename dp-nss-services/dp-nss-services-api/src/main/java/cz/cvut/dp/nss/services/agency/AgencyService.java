package cz.cvut.dp.nss.services.agency;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.List;

/**
 * Common interface for all AgencyService implementations.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public interface AgencyService extends EntityService<Agency, String> {

    /**
     * @param searchQuery retezec, ktery se vyskytuje v ID nebo nazvu dopravce
     * @return vsechny dopravce, kde id nebo nazev odpovida searchQuery
     */
    List<Agency> findAgenciesBySearchQuery(String searchQuery);

    /**
     * @param filter filter
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @param orderColumn radici sloupec
     * @param asc true pokud radim asc, false jinak (desc)
     * @return vsechny dopravce dle filtru
     */
    List<Agency> getByFilter(final AgencyFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc);

    /**
     * @return celkovy pocet zaznamu dle filtru (pro ucely strankovani)
     */
    long getCountByFilter(final AgencyFilter filter);

    /**
     * @param id id dopravce
     * @return true, pokud muze byt dopravce smazan (nema zadnou napojenou routu)
     */
    boolean canBeDeleted(String id);

}
