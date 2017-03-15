package cz.cvut.dp.nss.services.shape;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.List;

/**
 * Common interface for all ShapeService implementations.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
public interface ShapeService extends EntityService<Shape, ShapeId> {

    /**
     * smaze vsechny shapes s danym shapeId
     * @param id shapeId
     */
    void deleteByShapeId(String id);

    /**
     * @param id shapeId
     * @return vsechny shapes s danym shapeId. serazene dle sequence vzestupne
     */
    List<Shape> getByShapeId(String id);

    /**
     * @param idPattern pattern shapeId
     * @return vsechny shapes s shapeId odpovidajicim patternu. Serazene dle shapeId a nasledne dle sequnce vzestupne
     */
    List<String> findShapeIdsByLikeId(String idPattern);

    /**
     * @param filter filter
     * @param offset zacatek
     * @param limit max pocet  zaznamu unikatnich shapeId
     * @param orderColumn radici sloupec
     * @param asc true pokud asc order
     * @return vsechny shapeIds odpovidajici filtru serazene dle shapeId vzestupne nebo sestupne
     */
    List<String> getShapeIdsByFilter(ShapeFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc);

    /**
     * @param filter filter
     * @return pocet zaznamu unikatnich shapeId dle filtru
     */
    long getCountByFilter(ShapeFilter filter);

}
