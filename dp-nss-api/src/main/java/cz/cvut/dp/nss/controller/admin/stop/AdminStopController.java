package cz.cvut.dp.nss.controller.admin.stop;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopFilter;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stop.StopWheelchairBoardingType;
import cz.cvut.dp.nss.wrapper.output.stop.StopWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private static final String FILTER_NAME = "name";
    private static final String FILTER_LAT = "lat";
    private static final String FILTER_LON = "lon";
    private static final String FILTER_WHEEL_CHAIR = "wheelChairCode";
    private static final String FILTER_PARENT_STOP_ID = "parentStopId";

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<StopWrapper>> getStops(@RequestHeader(value = X_LIMIT_HEADER, required = false) Integer xLimit,
                                      @RequestHeader(value = X_OFFSET_HEADER, required = false) Integer xOffset,
                                      @RequestHeader(value = X_ORDER_HEADER, required = false) String xOrder,
                                      @RequestParam(value = FILTER_ID, required = false) String id,
                                      @RequestParam(value = FILTER_NAME, required = false) String name,
                                      @RequestParam(value = FILTER_LAT, required = false) Double lat,
                                      @RequestParam(value = FILTER_LON, required = false) Double lon,
                                      @RequestParam(value = FILTER_WHEEL_CHAIR, required = false) Integer wheelChairCode,
                                      @RequestParam(value = FILTER_PARENT_STOP_ID, required = false) String parentStopId,
                                      @RequestParam(value = FILTER_SEARCH_QUERY, required = false) String searchQuery) throws BadRequestException {

        List<Stop> stops;
        List<StopWrapper> wrappers = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        if(!StringUtils.isBlank(searchQuery)) {
            //searchQuery vsechno prebiji a nic jineho se neaplikuje
            stops = stopService.findStopsBySearchQuery(searchQuery);
        } else {
            final OrderWrapper order = getOrderFromHeader(xOrder);
            final StopFilter filter = getFilterFromParams(id, name, lat, lon, wheelChairCode, parentStopId);
            stops = stopService.getByFilter(filter, xOffset, xLimit, order.getOrderColumn(), order.isAsc());
            httpHeaders.add(X_COUNT_HEADER, stopService.getCountByFilter(filter) + "");
        }

        for(Stop stop : stops) {
            wrappers.add(getStopWrapper(stop));
        }

        return new ResponseEntity<>(wrappers, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public StopWrapper getStop(@PathVariable("id") String id) {
        Stop stop = stopService.get(id);
        if(stop == null) throw new ResourceNotFoundException();

        StopWrapper stopWrapper = getStopWrapper(stop);
        if(stopService.canBeDeleted(stop.getId())) stopWrapper.setCanBeDeleted(true);
        return stopWrapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StopWrapper> createStop(@RequestBody StopWrapper wrapper) throws BadRequestException {
        Stop stop = getStop(wrapper);
        stopService.create(stop);

        return getResponseCreated(getStopWrapper(stopService.get(stop.getId())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public StopWrapper updateStop(@PathVariable("id") String id, @RequestBody StopWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        Stop existingStop = stopService.get(id);
        if(existingStop == null) throw new ResourceNotFoundException();

        Stop stop = getStop(wrapper);
        stop.setId(id);
        //jmeno neni mozne updatovat, protoze je na tom zavisla struktura grafu neo4j
        stop.setName(existingStop.getName());
        stopService.update(stop, true);

        return getStopWrapper(stop);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteStop(@PathVariable("id") String id) throws BadRequestException {
        Stop stop = stopService.get(id);
        if(stop == null) {
            //ok, jiz neni v DB
            return;
        }

        if(!stopService.canBeDeleted(stop.getId())) {
            throw new BadRequestException("stop can not be deleted");
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
        wrapper.setWheelChairCode(stop.getStopWheelchairBoardingType() != null ? stop.getStopWheelchairBoardingType().getCode() : null);
        wrapper.setParentStopId(stop.getParentStop() != null ? stop.getParentStop().getId() : null);

        return wrapper;
    }

    private Stop getStop(StopWrapper wrapper) throws BadRequestException {
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
            Stop parentStop = stopService.get(wrapper.getParentStopId());
            if(parentStop == null) {
                throw new BadRequestException("unknown parent stop");
            }
            stop.setParentStop(parentStop);
        }

        return stop;
    }

    private static StopFilter getFilterFromParams(String id, String name, Double lat, Double lon, Integer wheelChairCode, String parentStopId) {
        StopFilter stopFilter = new StopFilter();
        stopFilter.setId(id);
        stopFilter.setName(name);
        stopFilter.setLat(lat);
        stopFilter.setLon(lon);
        stopFilter.setWheelChairCode(wheelChairCode);
        stopFilter.setParentStopId(parentStopId);

        return stopFilter;
    }
}
