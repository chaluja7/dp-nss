package cz.cvut.dp.nss.graph.services.calendarDate;

import cz.cvut.dp.nss.graph.repository.calendarDate.CalendarDateNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
@Service
public class CalendarDateNodeServiceImpl extends AbstractNodeService<CalendarDateNode, CalendarDateNodeRepository> implements CalendarDateNodeService {

    @Autowired
    public CalendarDateNodeServiceImpl(CalendarDateNodeRepository repository) {
        super(repository);
    }

    @Override
    public CalendarDateNode findByCalendarDateId(Long calendarDateId) {
        if(calendarDateId == null) return null;
        return repo.findByCalendarDateId(calendarDateId);
    }
}
