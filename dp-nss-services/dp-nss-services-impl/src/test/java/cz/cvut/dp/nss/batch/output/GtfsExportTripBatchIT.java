package cz.cvut.dp.nss.batch.output;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
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
public class GtfsExportTripBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "gtfsExportTripBatchJob")
    private Job gtfsExportTripBatchJob;

    @Test
    public void textExport() throws Throwable {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("exportFileLocation", new JobParameter(GTFS_OUT_LOCATION));
        parameters.put("schema", new JobParameter(SchemaThreadLocal.get()));

        JobExecution execution = jobLauncher.run(gtfsExportTripBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);
    }

}
