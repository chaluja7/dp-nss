package cz.cvut.dp.nss.controller.admin.timeTable;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.interceptor.SecurityInterceptor;
import cz.cvut.dp.nss.controller.timeTable.TimeTableController;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.exception.UnauthorizedException;
import cz.cvut.dp.nss.services.person.PersonService;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import cz.cvut.dp.nss.wrapper.output.timeTable.TimeTableWrapper;
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

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TimeTableWrapper> getTimeTables(@RequestHeader(SecurityInterceptor.SECURITY_HEADER) String userToken) {
        //admin vidi vsechno (i nevalidni), obycejny uzivatel jen ty, ktere ma prirazene (i nevalidni)
        List<TimeTableWrapper> timeTableWrappers = new ArrayList<>();
        for(TimeTable timeTable : timeTableService.getAllForPerson(personService.getByToken(userToken))) {
            timeTableWrappers.add(TimeTableController.getTimeTableWrapper(timeTable));
        }

        return timeTableWrappers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TimeTableWrapper getTimeTable(@PathVariable("id") String id, @RequestHeader(SecurityInterceptor.SECURITY_HEADER) String userToken) throws UnauthorizedException {
        //zaslouzilo by si vlastni anotaci a kontrolovat v CheckAccess
        if(!personOwnsTimeTable(personService.getByToken(userToken), id)) throw new UnauthorizedException();

        TimeTable timeTable = timeTableService.get(id);
        if(timeTable == null) {
            throw new ResourceNotFoundException();
        }

        return TimeTableController.getTimeTableWrapper(timeTable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TimeTableWrapper updateTimeTable(@PathVariable("id") String id, @RequestBody TimeTableWrapper wrapper,
                                            @RequestHeader(SecurityInterceptor.SECURITY_HEADER) String userToken) throws ResourceNotFoundException, BadRequestException, UnauthorizedException {
        //zaslouzilo by si vlastni anotaci a kontrolovat v CheckAccess
        if(!personOwnsTimeTable(personService.getByToken(userToken), id)) throw new UnauthorizedException();

        TimeTable currentTimeTable = timeTableService.get(id);
        if(currentTimeTable == null) throw new ResourceNotFoundException();

        TimeTable timeTable = getTimeTable(wrapper);
        timeTable.setId(id);
        timeTable.setSynchronizing(currentTimeTable.isSynchronizing());
        timeTable.setSynchronizingFailMessage(currentTimeTable.getSynchronizingFailMessage());
        timeTableService.update(timeTable);

        return TimeTableController.getTimeTableWrapper(timeTable);
    }

    public static TimeTable getTimeTable(TimeTableWrapper wrapper) {
        if(wrapper == null) return null;

        TimeTable timeTable = new TimeTable();
        timeTable.setId(wrapper.getId());
        timeTable.setName(wrapper.getName());
        timeTable.setValid(wrapper.isValid());
        timeTable.setMaxTravelTime(wrapper.getMaxTravelTime());

        return timeTable;
    }

}
