package cz.cvut.dp.nss.controller.timeTable;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import cz.cvut.dp.nss.wrapper.output.timeTable.TimeTableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller obsluhujici jizdni rady
 *
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

    /**
     * @param timeTable jizdni rad entita
     * @return jizdni rad wrapper
     */
    public static TimeTableWrapper getTimeTableWrapper(TimeTable timeTable) {
        if(timeTable == null) return null;

        TimeTableWrapper wrapper = new TimeTableWrapper();
        wrapper.setId(timeTable.getId());
        wrapper.setName(timeTable.getName());
        wrapper.setValid(timeTable.isValid());
        wrapper.setMaxTravelTime(timeTable.getMaxTravelTime());
        wrapper.setSynchronizing(timeTable.isSynchronizing());
        wrapper.setSynchronizationFailMessage(timeTable.getSynchronizingFailMessage());

        return wrapper;
    }

}
