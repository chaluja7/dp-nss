package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.persistence.timeTable.TimeTableDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.person.Person;
import cz.cvut.dp.nss.services.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of TripService.
 *
 * @author jakubchalupa
 * @since 02.03.17
 */
@Service
public class TimeTableServiceImpl extends AbstractEntityService<TimeTable, String, TimeTableDao> implements TimeTableService {

    @Autowired
    public TimeTableServiceImpl(TimeTableDao dao) {
        super(dao);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TimeTable> getAll(boolean validOnly) {
        return dao.getAll(validOnly);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TimeTable> getAllForPerson(Person person) {
        if(person == null) return new ArrayList<>();
        //admin ma pravo na vsechny
        if(person.hasRole(Role.Type.ADMIN)) return getAll(false);
        if(person.getTimeTables() == null || person.getTimeTables().isEmpty()) return new ArrayList<>();

        Set<String> timeTableIds = new HashSet<>();
        for(TimeTable timeTable : person.getTimeTables()) {
           timeTableIds.add(timeTable.getId());
        }

        return dao.getAllByIds(timeTableIds);
    }

}
