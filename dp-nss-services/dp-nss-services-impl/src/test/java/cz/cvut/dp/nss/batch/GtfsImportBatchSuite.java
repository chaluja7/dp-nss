package cz.cvut.dp.nss.batch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author jakubchalupa
 * @since 14.01.17
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    GtfsImportAgencyBatchIT.class, GtfsImportRouteBatchIT.class, GtfsImportCalendarBatchIT.class,
    GtfsImportCalendarDateBatchIT.class, GtfsImportStopBatchIT.class, GtfsImportShapeBatchIT.class,
    GtfsImportTripBatchIT.class, GtfsImportStopTimeBatchIT.class,
    GraphImportTripBatchIT.class, GraphConnectStopBatchIT.class
})
public class GtfsImportBatchSuite {

    //empty - slouzi jen jako spoustedlo pro suite classes

}
