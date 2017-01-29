package cz.cvut.dp.nss.batch;

import cz.cvut.dp.nss.services.AbstractServiceIT;
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
public class GraphConnectStopBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "graphConnectStopBatchJob")
    private Job graphConnectStopBatchJob;

    @Test
    public void testImport() throws Throwable {
        JobExecution execution = jobLauncher.run(graphConnectStopBatchJob, new JobParameters());
        failOnJobFailure(execution);
    }

}
