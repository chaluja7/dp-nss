package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.graph.services.calendar.CalendarNodeService;
import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNodeService;
import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNodeService;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
import cz.cvut.dp.nss.persistence.timeTable.TimeTableDao;
import cz.cvut.dp.nss.services.agency.AgencyService;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import cz.cvut.dp.nss.services.calendarDate.CalendarDateService;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import cz.cvut.dp.nss.services.person.Person;
import cz.cvut.dp.nss.services.role.Role;
import cz.cvut.dp.nss.services.route.RouteService;
import cz.cvut.dp.nss.services.shape.ShapeService;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stopTime.StopTimeService;
import cz.cvut.dp.nss.services.trip.TripService;
import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Implementation of TripService.
 *
 * @author jakubchalupa
 * @since 02.03.17
 */
@Service
public class TimeTableServiceImpl extends AbstractEntityService<TimeTable, String, TimeTableDao> implements TimeTableService {

    private static final Logger LOGGER = Logger.getLogger(TimeTableServiceImpl.class);

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
    public TimeTableServiceImpl(TimeTableDao dao) {
        super(dao);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TimeTable> getAll(boolean validOnly) {
        return dao.getAll(validOnly);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TimeTable> getAllForPerson(Person person) {
        if(person == null) return new ArrayList<>();
        //admin ma pravo na vsechny
        if(person.hasRole(Role.Type.ADMIN)) return getAll(false);
        if(person.getTimeTables() == null || person.getTimeTables().isEmpty()) return new ArrayList<>();

        Set<String> timeTableIds = new HashSet<>();
        for(TimeTable timeTable : person.getTimeTables()) {
           timeTableIds.add(timeTable.getId());
        }

        return dao.getAllByIds(timeTableIds);
    }

    @Async
    @Override
    public void generateTimeTableToDatabases(String schema, String folder) throws Throwable {
        String s = SchemaThreadLocal.get();

        //protoze toto bez v jinem vlakne, nez caller tak musim nastavit schema rucne (jinak by bylo null)
        SchemaThreadLocal.set(schema);
        //TODO zamknout schema

        try {
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
            throw(t);
        }

        //TODO odemknout schema. pri chybe toto zaznamenat (ze to dobehlo s chybou aby se to pak zobrazilo uzivateli)
        //TODO bylo by taky super smazat ty soubory, ktere uz jsou uplne k nicemu (v /tmp...)
        SchemaThreadLocal.unset();
    }

    public static void failOnJobFailure(JobExecution jobExecution) throws Throwable {
        List<Throwable> allFailureExceptions = jobExecution.getAllFailureExceptions();
        if(allFailureExceptions != null && !allFailureExceptions.isEmpty()) {
            throw allFailureExceptions.get(0);
        }
    }

}
