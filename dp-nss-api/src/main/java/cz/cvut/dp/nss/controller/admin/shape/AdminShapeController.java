package cz.cvut.dp.nss.controller.admin.shape;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.shape.Shape;
import cz.cvut.dp.nss.services.shape.ShapeFilter;
import cz.cvut.dp.nss.services.shape.ShapeId;
import cz.cvut.dp.nss.services.shape.ShapeService;
import cz.cvut.dp.nss.wrapper.output.shape.ShapeWrapper;
import cz.cvut.dp.nss.wrapper.output.shape.ShapesWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jakubchalupa
 * @since 12.03.17
 */
@RestController
@RequestMapping(value = "/admin/shape")
public class AdminShapeController extends AdminAbstractController {

    @Autowired
    private ShapeService shapeService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ShapesWrapper>> getShapes(@RequestHeader(value = X_LIMIT_HEADER, required = false) Integer xLimit,
                                      @RequestHeader(value = X_OFFSET_HEADER, required = false) Integer xOffset,
                                      @RequestHeader(value = X_ORDER_HEADER, required = false) String xOrder,
                                      @RequestParam(value = FILTER_ID, required = false) String id,
                                      @RequestParam(value = FILTER_SEARCH_QUERY, required = false) String searchQuery) throws BadRequestException {

        List<String> shapeIds;
        HttpHeaders httpHeaders = new HttpHeaders();
        if(!StringUtils.isBlank(searchQuery)) {
            //searchQuery vsechno prebiji a nic jineho se neaplikuje
            shapeIds = shapeService.findShapeIdsByLikeId(searchQuery);
        } else {
            final OrderWrapper order = getOrderFromHeader(xOrder);
            final ShapeFilter filter = getFilterFromParams(id);
            shapeIds = shapeService.getShapeIdsByFilter(filter, xOffset, xLimit, order.getOrderColumn(), order.isAsc());
            httpHeaders.add(X_COUNT_HEADER, shapeService.getCountByFilter(filter) + "");
        }

        List<ShapesWrapper> wrappers = new ArrayList<>();
        for(String shapeId : shapeIds) {
            ShapesWrapper wrapper = new ShapesWrapper();
            wrapper.setId(shapeId);
            wrappers.add(wrapper);
        }

        return new ResponseEntity<>(wrappers, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ShapesWrapper getShape(@PathVariable("id") String id) {
        List<Shape> shapes = shapeService.getByShapeId(id);
        if(shapes == null || shapes.isEmpty()) throw new ResourceNotFoundException();

        //urcite tam bude prave jeden zaznam
        ShapesWrapper wrapper = createShapesWrappers(shapes).get(0);
        //muze byt smazano vzdy
        wrapper.setCanBeDeleted(true);
        return wrapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ShapesWrapper> createShape(@RequestBody ShapesWrapper wrapper) throws BadRequestException {
        List<Shape> shapes = getShapes(wrapper);
        //smazu vsechny s timto shapeId
        shapeService.deleteByShapeId(wrapper.getId());
        //a znovu je ulozim
        for(Shape shape : shapes) {
            shapeService.create(shape);
        }

        return getResponseCreated(createShapesWrappers(shapeService.getByShapeId(wrapper.getId())).get(0));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ShapesWrapper updateShape(@PathVariable("id") String id, @RequestBody ShapesWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        List<Shape> existingShapes = shapeService.getByShapeId(id);
        if(existingShapes == null || existingShapes.isEmpty()) throw new ResourceNotFoundException();

        List<Shape> shapes = getShapes(wrapper);
        //smazu vsechny s timto shapeId
        shapeService.deleteByShapeId(wrapper.getId());

        //a znovu je ulozim
        for(Shape shape : shapes) {
            shapeService.create(shape);
        }

        return createShapesWrappers(shapes).get(0);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteShape(@PathVariable("id") String id) throws BadRequestException {
        List<Shape> existingShapes = shapeService.getByShapeId(id);
        if(existingShapes == null || existingShapes.isEmpty()) {
            //ok, jiz neni v DB
            return;
        }

        shapeService.deleteByShapeId(id);
    }

    /**
     * @param shapes - musi byt serazeno dle shapeId a nasledne dle sequence!
     * @return list shapesWrapper
     */
    private static List<ShapesWrapper> createShapesWrappers(List<Shape> shapes) {
        if(shapes == null) return null;

        List<ShapesWrapper> wrappers = new ArrayList<>();
        Map<String, List<ShapeWrapper>> map = new LinkedHashMap<>();

        for(Shape shape : shapes) {
            ShapeWrapper wrapper = getShapeWrapper(shape);

            if(!map.containsKey(shape.getId().getShapeId())) {
                map.put(shape.getId().getShapeId(), new ArrayList<>());
            }

            map.get(shape.getId().getShapeId()).add(wrapper);
        }

        for(Map.Entry<String, List<ShapeWrapper>> entry : map.entrySet()) {
           ShapesWrapper wrapper = new ShapesWrapper();
           wrapper.setId(entry.getKey());
           wrapper.setShapes(entry.getValue());

           wrappers.add(wrapper);
        }

        return wrappers;
    }

    public static ShapeWrapper getShapeWrapper(Shape shape) {
        if(shape == null) return null;

        ShapeWrapper wrapper = new ShapeWrapper();
        wrapper.setSequence(shape.getId().getSequence());
        wrapper.setLat(shape.getLat());
        wrapper.setLon(shape.getLon());

        return wrapper;
    }

    private List<Shape> getShapes(ShapesWrapper wrapper) throws BadRequestException {
        if(wrapper == null) return null;

        List<Shape> shapes = new ArrayList<>();
        for(ShapeWrapper shapeWrapper : wrapper.getShapes()) {
           shapes.add(getShape(wrapper.getId(), shapeWrapper));
        }

        return shapes;
    }

    private Shape getShape(String shapeId, ShapeWrapper wrapper) {
        if(shapeId == null || wrapper == null) return null;

        Shape shape = new Shape();
        shape.setId(new ShapeId(shapeId, wrapper.getSequence()));
        shape.setLat(wrapper.getLat());
        shape.setLon(wrapper.getLon());

        return shape;
    }

    private static ShapeFilter getFilterFromParams(String id) {
        ShapeFilter shapeFilter = new ShapeFilter();
        shapeFilter.setId(id);

        return shapeFilter;
    }
}
