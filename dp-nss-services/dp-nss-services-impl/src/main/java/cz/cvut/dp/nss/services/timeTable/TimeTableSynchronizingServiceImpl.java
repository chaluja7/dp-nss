package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.context.ThreadScope;
import cz.cvut.dp.nss.graph.services.calendar.CalendarNodeService;
import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNodeService;
import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNodeService;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
import cz.cvut.dp.nss.services.agency.AgencyService;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import cz.cvut.dp.nss.services.calendarDate.CalendarDateService;
import cz.cvut.dp.nss.services.route.RouteService;
import cz.cvut.dp.nss.services.shape.ShapeService;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stopTime.StopTimeService;
import cz.cvut.dp.nss.services.trip.TripService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of TripService.
 *
 * @author jakubchalupa
 * @since 02.03.17
 */
@Service
public class TimeTableSynchronizingServiceImpl implements TimeTableSynchronizingService {

    private static final Logger LOGGER = LogManager.getLogger(TimeTableSynchronizingServiceImpl.class);

    /**
     * mapa ocekavanych souboru jizdniho radu. Hodnota je, zda je polozka povinna.
     */
    public static final Map<String, Boolean> TIME_TABLE_FILES;

    static {
        TIME_TABLE_FILES = new HashMap<>();
        TIME_TABLE_FILES.put("agency.txt", true);
        TIME_TABLE_FILES.put("calendar.txt", true);
        TIME_TABLE_FILES.put("calendar_dates.txt", false);
        TIME_TABLE_FILES.put("routes.txt", true);
        TIME_TABLE_FILES.put("shapes.txt", false);
        TIME_TABLE_FILES.put("stop_times.txt", true);
        TIME_TABLE_FILES.put("stops.txt", true);
        TIME_TABLE_FILES.put("trips.txt", true);
    }

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CalendarDateService calendarDateService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShapeService shapeService;

    @Autowired
    private StopService stopService;

    @Autowired
    private StopTimeService stopTimeService;

    @Autowired
    private TripService tripService;

    @Autowired
    private CalendarDateNodeService calendarDateNodeService;

    @Autowired
    private CalendarNodeService calendarNodeService;

    @Autowired
    private TripNodeService tripNodeService;

    @Autowired
    private StopTimeNodeService stopTimeNodeService;

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "gtfsImportAgencyBatchJob")
    private Job gtfsImportAgencyBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportCalendarBatchJob")
    private Job gtfsImportCalendarBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportCalendarDateBatchJob")
    private Job gtfsImportCalendarDateBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportRouteBatchJob")
    private Job gtfsImportRouteBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportShapeBatchJob")
    private Job gtfsImportShapeBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportStopParentBatchJob")
    private Job gtfsImportStopParentBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportStopChildBatchJob")
    private Job gtfsImportStopChildBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportTripBatchJob")
    private Job gtfsImportTripBatchJob;

    @Autowired
    @Qualifier(value = "gtfsImportStopTimeBatchJob")
    private Job gtfsImportStopTimeBatchJob;

    @Autowired
    @Qualifier(value = "graphImportCalendarBatchJob")
    private Job graphImportCalendarBatchJob;

    @Autowired
    @Qualifier(value = "graphImportTripBatchJob")
    private Job graphImportTripBatchJob;

    @Autowired
    @Qualifier(value = "graphConnectStopBatchJob")
    private Job graphConnectStopBatchJob;

    @Autowired
    private ThreadScope threadScope;

    @Async
    @Override
    public void generateTimeTableToDatabases(String schema, String folder) throws Throwable {
        TimeTable timeTable;
        Throwable throwable = null;
        try {
            //protoze toto bez v jinem vlakne, nez caller tak musim nastavit schema rucne (jinak by bylo null)
            SchemaThreadLocal.set(schema);

            //zamknu schema
            timeTable = timeTableService.get(schema);
            timeTable.setSynchronizing(true);
            timeTable.setSynchronizingFailMessage(null);
            timeTableService.update(timeTable);

            //vyprazdnim vsechny zaznamy jizdniho radu
            stopTimeService.truncateAll();
            tripService.truncateAll();
            shapeService.truncateAll();
            stopService.truncateAll();
            calendarDateService.truncateAll();
            calendarService.truncateAll();
            routeService.truncateAll();
            agencyService.truncateAll();

            //taky z neo4j
            calendarDateNodeService.deleteAll();
            calendarNodeService.deleteAll();
            tripNodeService.deleteAll();
            stopTimeNodeService.deleteAll();

            //a ve spravnem poradi spustim joby
            Map<String, JobParameter> parameters = new HashMap<>();
            parameters.put("importFolderLocation", new JobParameter(folder));

            JobExecution execution = jobLauncher.run(gtfsImportAgencyBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportRouteBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportCalendarBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportCalendarDateBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportStopParentBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportStopChildBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportShapeBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportTripBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(gtfsImportStopTimeBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            //neo4j joby
            execution = jobLauncher.run(graphImportCalendarBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(graphImportTripBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

            execution = jobLauncher.run(graphConnectStopBatchJob, new JobParameters(parameters));
            failOnJobFailure(execution);

        } catch(Throwable t) {
            LOGGER.error("", t);
            throwable = t;
        } finally {
            try {
                String msg = null;
                if (throwable != null) {
                    msg = throwable.getMessage();
                    if (msg != null && msg.length() > 4096) msg = msg.substring(0, 4096);
                }

                timeTable = timeTableService.get(schema);
                timeTable.setSynchronizing(false);
                timeTable.setSynchronizingFailMessage(msg);
                timeTableService.update(timeTable);
            } finally {
                SchemaThreadLocal.unset();
                threadScope.cleanUpThreadScopedBeans();

                if(throwable != null) {
                    throw(throwable);
                }
            }
        }

        //TODO bylo by taky super smazat ty soubory, ktere uz jsou uplne k nicemu (v /tmp...)
    }

    public static void failOnJobFailure(JobExecution jobExecution) throws Throwable {
        List<Throwable> allFailureExceptions = jobExecution.getAllFailureExceptions();
        if(allFailureExceptions != null && !allFailureExceptions.isEmpty()) {
            throw allFailureExceptions.get(0);
        }
    }

}
