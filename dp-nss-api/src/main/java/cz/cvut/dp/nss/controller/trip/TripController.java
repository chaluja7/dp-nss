package cz.cvut.dp.nss.controller.trip;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.trip.AdminTripController;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.shape.Shape;
import cz.cvut.dp.nss.services.shape.ShapeService;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripService;
import cz.cvut.dp.nss.wrapper.output.trip.TripWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@RestController
@RequestMapping(value = "/trip")
public class TripController extends AbstractController {

    @Autowired
    private TripService tripService;

    @Autowired
    private ShapeService shapeService;

    /**
     * @param tripId tripId
     * @return kompletni detail tripu s danym id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TripWrapper findTrip(@PathVariable("id") String tripId, @RequestParam(name = "withShapes", required = false) Boolean withShapes) {
        Trip trip = tripService.getLazyInitialized(tripId);
        if(trip == null) throw new ResourceNotFoundException();

        List<Shape> shapes = null;
        if(Boolean.TRUE.equals(withShapes) && trip.getShapeId() != null) {
            shapes = shapeService.getByShapeId(trip.getShapeId());
        }

        return AdminTripController.getTripWrapper(trip, true, true, shapes);
    }

}
