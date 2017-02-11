package cz.cvut.dp.nss.batch;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNodeService;
import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNodeService;
import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
public class GraphImportCalendarBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "graphImportCalendarBatchJob")
    private Job graphImportCalendarBatchJob;

    @Autowired
    private CalendarNodeService calendarNodeService;

    @Autowired
    private CalendarDateNodeService calendarDateNodeService;

    @Before
    public void setUp() {
        super.before();
        calendarDateNodeService.deleteAll();
        calendarNodeService.deleteAll();
    }

    @Test
    public void testImport() throws Throwable {
        JobExecution execution = jobLauncher.run(graphImportCalendarBatchJob, new JobParameters());
        failOnJobFailure(execution);
    }

    @Test
    public void testNeco() {
        //empty - slouzi jen pro prime zavolani, cimz dojde k vykonani @Before bloku
    }

}
