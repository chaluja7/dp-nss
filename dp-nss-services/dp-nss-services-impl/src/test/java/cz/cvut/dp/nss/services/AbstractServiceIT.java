package cz.cvut.dp.nss.services;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Abstract service intergration test.
 *
 * @author jakubchalupa
 * @since 15.05.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
@Transactional
public abstract class AbstractServiceIT {

    protected static final String GTFS_IN_LOCATION = "/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/test/annapolis-transit_20150811_1647";
//    protected static final String GTFS_IN_LOCATION = "/Users/jakubchalupa/Documents/FEL/MGR/DP/gtfs/pid2017/jrdata";
    protected static final String GTFS_OUT_LOCATION = "/tmp/testExportGtfs";

    @BeforeClass
    public static void before() {
        SchemaThreadLocal.set(SchemaThreadLocal.SCHEMA_ANNAPOLIS);
    }

    @AfterClass
    public static void after() {
        //nezbytne!
        SchemaThreadLocal.unset();
    }

    protected void failOnJobFailure(JobExecution jobExecution) throws Throwable {
        List<Throwable> allFailureExceptions = jobExecution.getAllFailureExceptions();
        if(allFailureExceptions != null && !allFailureExceptions.isEmpty()) {
            throw allFailureExceptions.get(0);
        }
    }

}

