package cz.cvut.dp.nss.batch;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
public class GtfsImportStopBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "gtfsImportStopParentBatchJob")
    private Job gtfsImportStopParentBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportStopChildBatchJob")
    private Job gtfsImportStopChildBatchJob;

    @Test
    public void testImport() throws Throwable {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("stopsLocation", new JobParameter(GTFS_LOCATION + "stops.txt"));

        JobExecution execution = jobLauncher.run(gtfsImportStopParentBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsImportStopChildBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);
    }

}
