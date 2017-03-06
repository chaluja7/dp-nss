package cz.cvut.dp.nss.controller.timeTable;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import cz.cvut.dp.nss.wrapper.out.timeTable.TimeTableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author jakubchalupa
 * @since 02.03.17
 */
@RestController
@RequestMapping(value = "/timeTable")
public class TimeTableController extends AbstractController {

    @Autowired
    protected TimeTableService timeTableService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TimeTableWrapper> getTimeTables(@RequestParam(value = "validOnly", required = false) Boolean validOnly) {
        List<TimeTableWrapper> timeTableWrappers = new ArrayList<>();
        for(TimeTable timeTable : timeTableService.getAll(validOnly != null ? validOnly : false)) {
            timeTableWrappers.add(getTimeTableWrapper(timeTable));
        }

        return timeTableWrappers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TimeTableWrapper getTimeTable(@PathVariable("id") String id) {
        TimeTable timeTable = timeTableService.get(id);
        if(timeTable == null) {
            throw new ResourceNotFoundException();
        }

        return getTimeTableWrapper(timeTable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TimeTableWrapper> createTimeTable(@RequestBody TimeTableWrapper wrapper) {
        TimeTable timeTable = getTimeTable(wrapper);
        timeTableService.create(timeTable);

        return getResponseCreated(getTimeTableWrapper(timeTableService.get(timeTable.getId())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TimeTableWrapper updateTimeTable(@PathVariable("id") String id, @RequestBody TimeTableWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        if(timeTableService.get(id) == null) throw new ResourceNotFoundException();

        TimeTable timeTable = getTimeTable(wrapper);
        timeTable.setId(id);
        timeTableService.update(timeTable);

        return getTimeTableWrapper(timeTable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTimeTable(@PathVariable("id") String id) {
        TimeTable timeTable = timeTableService.get(id);
        if(timeTable == null) {
            //ok, jiz neni v DB
            return;
        }

        timeTableService.delete(timeTable.getId());
    }

    public static TimeTableWrapper getTimeTableWrapper(TimeTable timeTable) {
        if(timeTable == null) return null;

        TimeTableWrapper wrapper = new TimeTableWrapper();
        wrapper.setEntityId(timeTable.getId());
        wrapper.setName(timeTable.getName());
        wrapper.setValid(timeTable.isValid());

        return wrapper;
    }

    public static TimeTable getTimeTable(TimeTableWrapper wrapper) {
        if(wrapper == null) return null;

        TimeTable timeTable = new TimeTable();
        timeTable.setId(wrapper.getEntityId());
        timeTable.setName(wrapper.getName());
        timeTable.setValid(wrapper.isValid());

        return timeTable;
    }

}
