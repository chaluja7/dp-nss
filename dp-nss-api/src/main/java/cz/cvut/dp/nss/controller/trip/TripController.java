package cz.cvut.dp.nss.controller.trip;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.trip.AdminTripController;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripService;
import cz.cvut.dp.nss.wrapper.output.trip.TripWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@RestController
@RequestMapping(value = "/trip")
public class TripController extends AbstractController {

    @Autowired
    private TripService tripService;

    /**
     * @param tripId tripId
     * @return kompletni detail tripu s danym id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TripWrapper findTrip(@PathVariable("id") String tripId) {
        Trip trip = tripService.getLazyInitialized(tripId);
        if(trip == null) throw new ResourceNotFoundException();
        return AdminTripController.getTripWrapper(trip, true, true);
    }

}
