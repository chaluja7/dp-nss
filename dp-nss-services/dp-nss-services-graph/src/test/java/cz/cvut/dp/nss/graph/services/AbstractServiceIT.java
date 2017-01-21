package cz.cvut.dp.nss.graph.services;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * Abstract service intergration test.
 *
 * @author jakubchalupa
 * @since 15.05.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
@WebAppConfiguration
public abstract class AbstractServiceIT {

}

