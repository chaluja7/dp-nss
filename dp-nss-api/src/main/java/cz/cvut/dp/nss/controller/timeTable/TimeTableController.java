package cz.cvut.dp.nss.controller.timeTable;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import cz.cvut.dp.nss.wrapper.out.timeTable.TimeTableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public List<TimeTableWrapper> getTimeTables() {
        List<TimeTableWrapper> timeTableWrappers = new ArrayList<>();
        //zde vybiram jen validni
        for(TimeTable timeTable : timeTableService.getAll(true)) {
            timeTableWrappers.add(getTimeTableWrapper(timeTable));
        }

        return timeTableWrappers;
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
