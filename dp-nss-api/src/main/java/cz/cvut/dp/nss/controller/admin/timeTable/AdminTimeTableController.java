package cz.cvut.dp.nss.controller.admin.timeTable;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.timeTable.TimeTableController;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import cz.cvut.dp.nss.wrapper.out.timeTable.TimeTableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
@RestController
@RequestMapping(value = "/admin/timeTable")
public class AdminTimeTableController extends AdminAbstractController {

    @Autowired
    private TimeTableService timeTableService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TimeTableWrapper> getTimeTables() {
        List<TimeTableWrapper> timeTableWrappers = new ArrayList<>();
        //zde vybiram i nevalidni
        for(TimeTable timeTable : timeTableService.getAll(false)) {
            timeTableWrappers.add(TimeTableController.getTimeTableWrapper(timeTable));
        }

        return timeTableWrappers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TimeTableWrapper getTimeTable(@PathVariable("id") String id) {
        TimeTable timeTable = timeTableService.get(id);
        if(timeTable == null) {
            throw new ResourceNotFoundException();
        }

        return TimeTableController.getTimeTableWrapper(timeTable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TimeTableWrapper updateTimeTable(@PathVariable("id") String id, @RequestBody TimeTableWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        if(timeTableService.get(id) == null) throw new ResourceNotFoundException();

        TimeTable timeTable = TimeTableController.getTimeTable(wrapper);
        timeTable.setId(id);
        timeTableService.update(timeTable);

        return TimeTableController.getTimeTableWrapper(timeTable);
    }

}
