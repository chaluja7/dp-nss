package cz.cvut.dp.nss.controller.admin.stop;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stop.StopWheelchairBoardingType;
import cz.cvut.dp.nss.wrapper.out.stop.StopWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
@RestController
@RequestMapping(value = "/admin/stop")
public class AdminStopController extends AdminAbstractController {

    @Autowired
    private StopService stopService;

    @RequestMapping(method = RequestMethod.GET)
    public List<StopWrapper> getStops() {
        List<Stop> stops = stopService.getAllInRange(0, 100);
        List<StopWrapper> wrappers = new ArrayList<>();
        for(Stop stop : stops) {
            wrappers.add(getStopWrapper(stop));
        }

        return wrappers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public StopWrapper getStop(@PathVariable("id") String id) {
        Stop stop = stopService.get(id);
        if(stop == null) throw new ResourceNotFoundException();

        return getStopWrapper(stop);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StopWrapper> createStop(@RequestBody StopWrapper wrapper) {
        Stop stop = getStop(wrapper);
        stopService.create(stop);

        //TODO validacni chyby pri ukladani?

        return getResponseCreated(getStopWrapper(stopService.get(stop.getId())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public StopWrapper updateStop(@PathVariable("id") String id, @RequestBody StopWrapper wrapper) throws ResourceNotFoundException {
        if(stopService.get(id) == null) throw new ResourceNotFoundException();

        Stop stop = getStop(wrapper);
        stop.setId(id);
        stopService.update(stop);

        return getStopWrapper(stop);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteStop(@PathVariable("id") String id) {
        Stop stop = stopService.get(id);
        if(stop == null) {
            //ok, jiz neni v DB
            return;
        }

        stopService.delete(stop.getId());
    }

    public static StopWrapper getStopWrapper(Stop stop) {
        if(stop == null) return null;

        StopWrapper wrapper = new StopWrapper();
        wrapper.setId(stop.getId());
        wrapper.setName(stop.getName());
        wrapper.setLat(stop.getLat());
        wrapper.setLon(stop.getLon());
        if(stop.getStopWheelchairBoardingType() != null) {
            wrapper.setWheelChairCode(stop.getStopWheelchairBoardingType().getCode());
        }
        if(stop.getParentStop() != null) {
            wrapper.setParentStopId(stop.getParentStop().getId());
        }

        return wrapper;
    }

    private Stop getStop(StopWrapper wrapper) {
        if(wrapper == null) return null;

        Stop stop = new Stop();
        stop.setId(wrapper.getId());
        stop.setName(wrapper.getName());
        stop.setLat(wrapper.getLat());
        stop.setLon(wrapper.getLon());
        if(wrapper.getWheelChairCode() != null) {
            stop.setStopWheelchairBoardingType(EnumUtils.fromCode(wrapper.getWheelChairCode(), StopWheelchairBoardingType.class));
        }
        if(wrapper.getParentStopId() != null) {
            stop.setParentStop(stopService.get(wrapper.getParentStopId()));
        }

        return stop;
    }

}
