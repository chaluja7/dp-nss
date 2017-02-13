package cz.cvut.dp.nss.batch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author jakubchalupa
 * @since 14.01.17
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({GraphImportCalendarBatchIT.class, GraphImportTripBatchIT.class, GraphConnectStopBatchIT.class})
public class GtfsImportGraphBatchSuite {

    //empty - slouzi jen jako spoustedlo pro suite classes

}