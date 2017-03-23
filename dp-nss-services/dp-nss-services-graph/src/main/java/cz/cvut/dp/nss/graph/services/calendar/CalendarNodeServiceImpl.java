package cz.cvut.dp.nss.graph.services.calendar;

import cz.cvut.dp.nss.graph.repository.calendar.CalendarNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
@Service
public class CalendarNodeServiceImpl extends AbstractNodeService<CalendarNode, CalendarNodeRepository> implements CalendarNodeService {

    @Autowired
    protected Session session;

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

    @Override
    public void initCalendarDates() {
        session.query("CALL cz.cvut.dp.nss.search.initCalendarDates()", new HashMap<>(), true);
    }

}
