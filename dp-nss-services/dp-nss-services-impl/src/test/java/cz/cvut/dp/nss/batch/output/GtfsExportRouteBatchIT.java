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
public class GtfsExportRouteBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "gtfsExportRouteBatchJob")
    private Job gtfsExportRouteBatchJob;

    @Test
    public void textExport() throws Throwable {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("exportFileLocation", new JobParameter("/tmp/"));
        parameters.put("schema", new JobParameter(SchemaThreadLocal.get()));

        JobExecution execution = jobLauncher.run(gtfsExportRouteBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);
    }

}
