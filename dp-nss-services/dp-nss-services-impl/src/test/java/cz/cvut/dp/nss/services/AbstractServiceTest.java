package cz.cvut.dp.nss.services;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract service test.
 *
 * @author jakubchalupa
 * @since 15.05.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
@Transactional(transactionManager = "transactionManager")
public abstract class AbstractServiceTest {

}

