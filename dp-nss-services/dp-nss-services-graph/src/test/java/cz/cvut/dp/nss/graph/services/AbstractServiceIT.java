package cz.cvut.dp.nss.graph.services;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Abstract service intergration test.
 *
 * @author jakubchalupa
 * @since 15.05.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-graph-context.xml"})
public abstract class AbstractServiceIT {

    @Before
    public void before() {
        SchemaThreadLocal.set(SchemaThreadLocal.SCHEMA_VEDOUCI);
    }

    @After
    public void after() {
        //nezbytne!
        SchemaThreadLocal.unset();
    }

}

