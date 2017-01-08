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
public class GtfsImportBatchIT extends AbstractServiceIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "gtfsImportBatchJob")
    private Job gtfsImportBatchJob;

    @Test
    public void testNeco() {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("agencyLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/agency.txt"));
        parameters.put("routesLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/routes.txt"));
        parameters.put("calendarLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/calendar.txt"));
        parameters.put("calendarDateLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/calendarDates.txt"));
        parameters.put("shapesLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/shapes.txt"));
        parameters.put("stopsLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/stops.txt"));
        parameters.put("tripsLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/trips.txt"));
        parameters.put("stopTimesLocation", new JobParameter("file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid-2013/data/stopTimes.txt"));

        try {
            JobExecution execution = jobLauncher.run(gtfsImportBatchJob, new JobParameters(parameters));
        } catch(Exception e) {
            int i = 0;
        }

        int i = 0;
    }



}
