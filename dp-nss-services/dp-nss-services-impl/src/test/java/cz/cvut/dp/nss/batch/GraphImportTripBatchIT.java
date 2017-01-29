package cz.cvut.dp.nss.batch;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNodeService;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
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
public class GraphImportTripBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "graphImportTripBatchJob")
    private Job graphImportTripBatchJob;

    @Autowired
    private TripNodeService tripNodeService;

    @Autowired
    private StopTimeNodeService stopTimeNodeService;

    @Before
    public void setUp() {
        super.before();
        tripNodeService.deleteAll();
        stopTimeNodeService.deleteAll();
    }

    @Test
    public void testImport() throws Throwable {
        JobExecution execution = jobLauncher.run(graphImportTripBatchJob, new JobParameters());
        failOnJobFailure(execution);
    }

}
