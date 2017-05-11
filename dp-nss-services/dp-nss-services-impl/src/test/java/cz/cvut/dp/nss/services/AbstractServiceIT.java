package cz.cvut.dp.nss.services;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Abstract service intergration test.
 *
 * @author jakubchalupa
 * @since 15.05.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public abstract class AbstractServiceIT {

    @Value("${gtfs.test.in.location}")
    protected String GTFS_IN_LOCATION;

    @Value("${gtfs.test.out.location}")
    protected String GTFS_OUT_LOCATION;

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
            throw allFailureExceptions.get(0);
        }
    }

}

