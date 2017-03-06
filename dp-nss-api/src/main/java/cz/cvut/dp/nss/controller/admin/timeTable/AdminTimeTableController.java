package cz.cvut.dp.nss.controller.admin.timeTable;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.timeTable.TimeTableController;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import cz.cvut.dp.nss.wrapper.out.timeTable.TimeTableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
@RestController
@RequestMapping(value = "/admin/timeTable")
public class AdminTimeTableController extends AdminAbstractController {

    @Autowired
    private TimeTableService timeTableService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TimeTableWrapper getTimeTable(@PathVariable("id") String id) {
        TimeTable timeTable = timeTableService.get(id);
        if(timeTable == null) {
            throw new ResourceNotFoundException();
        }

        return TimeTableController.getTimeTableWrapper(timeTable);
    }

}
