package cz.cvut.dp.nss.batch.output;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author jakubchalupa
 * @since 14.01.17
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    GtfsExportAgencyBatchIT.class, GtfsExportCalendarBatchIT.class, GtfsExportCalendarDateBatchIT.class,
    GtfsExportShapeBatchIT.class, GtfsExportStopBatchIT.class, GtfsExportStopTimeBatchIT.class, GtfsExportTripBatchIT.class,
    GtfsExportRouteBatchIT.class
})
public class GtfsExportBatchSuite {

    //empty - slouzi jen jako spoustedlo pro suite classes

}