package cz.cvut.dp.nss.batch.input;

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
public class GtfsImportCalendarBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "gtfsImportCalendarBatchJob")
    private Job gtfsImportCalendarBatchJob;

    @Test
    public void testImport() throws Throwable {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("importFolderLocation", new JobParameter(GTFS_IN_LOCATION));

        JobExecution execution = jobLauncher.run(gtfsImportCalendarBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);
    }

}
