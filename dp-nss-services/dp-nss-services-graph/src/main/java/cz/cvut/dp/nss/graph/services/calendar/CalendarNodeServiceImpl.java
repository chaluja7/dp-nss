package cz.cvut.dp.nss.graph.services.calendar;

import cz.cvut.dp.nss.graph.repository.calendar.CalendarNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
@Service
public class CalendarNodeServiceImpl extends AbstractNodeService<CalendarNode, CalendarNodeRepository> implements CalendarNodeService {

    @Autowired
    public CalendarNodeServiceImpl(CalendarNodeRepository repository) {
        super(repository);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public CalendarNode findByCalendarId(String calendarId) {
        if(calendarId == null) return null;
        return repo.findByCalendarId(calendarId);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void deleteAll() {
        repo.deleteAll();
    }

}
