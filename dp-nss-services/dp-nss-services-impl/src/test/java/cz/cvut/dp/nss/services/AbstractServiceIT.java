package cz.cvut.dp.nss.services;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;


/**
 * Abstract service intergration test.
 *
 * @author jakubchalupa
 * @since 15.05.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
//@Transactional(transactionManager = "transactionManager")
@WebAppConfiguration
public abstract class AbstractServiceIT {

    protected static final String GTFS_IN_LOCATION = "/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/test/annapolis-transit_20150811_1647";
//    protected static final String GTFS_IN_LOCATION = "file:/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid2017/jrdata/";
    protected static final String GTFS_OUT_LOCATION = "/tmp/testExportGtfs";

    @Before
    public void before() {
        SchemaThreadLocal.set(SchemaThreadLocal.SCHEMA_ANNAPOLIS);
    }

    @After
    public void after() {
        //nezbytne!
        SchemaThreadLocal.unset();
    }

    protected void failOnJobFailure(JobExecution jobExecution) throws Throwable {
        List<Throwable> allFailureExceptions = jobExecution.getAllFailureExceptions();
        if(allFailureExceptions != null && !allFailureExceptions.isEmpty()) {
            //todo nekontrolovat exit status?
            throw allFailureExceptions.get(0);
        }
    }

}

