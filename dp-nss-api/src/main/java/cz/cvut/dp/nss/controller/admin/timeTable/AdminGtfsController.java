package cz.cvut.dp.nss.controller.admin.timeTable;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
import cz.cvut.dp.nss.services.timeTable.TimeTableServiceImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author jakubchalupa
 * @since 17.03.17
 */
@RestController
@RequestMapping(value = "/admin/gtfs")
public class AdminGtfsController extends AdminAbstractController {

    @Value("${gtfs.out.location}")
    private String gtfsOutLocation;

    @Value("${gtfs.in.location}")
    private String gtfsInLocation;

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "gtfsExportAgencyBatchJob")
    private Job gtfsExportAgencyBatchJob;

    @Autowired
    @Qualifier(value = "gtfsExportCalendarBatchJob")
    private Job gtfsExportCalendarBatchJob;

    @Autowired
    @Qualifier(value = "gtfsExportCalendarDateBatchJob")
    private Job gtfsExportCalendarDateBatchJob;

    @Autowired
    @Qualifier(value = "gtfsExportRouteBatchJob")
    private Job gtfsExportRouteBatchJob;

    @Autowired
    @Qualifier(value = "gtfsExportShapeBatchJob")
    private Job gtfsExportShapeBatchJob;

    @Autowired
    @Qualifier(value = "gtfsExportStopTimeBatchJob")
    private Job gtfsExportStopTimeBatchJob;

    @Autowired
    @Qualifier(value = "gtfsExportStopBatchJob")
    private Job gtfsExportStopBatchJob;

    @Autowired
    @Qualifier(value = "gtfsExportTripBatchJob")
    private Job gtfsExportTripBatchJob;

    @RequestMapping(value = "/download")
    public ResponseEntity<StreamingResponseBody> downloadTimeTableGTFS() throws Throwable {
        final String schema = SchemaThreadLocal.get();
        Assert.notNull(schema, "schema must be specified");

        final String folder = schema + "-" + UUID.randomUUID().toString();
        File baseFile = new File(gtfsOutLocation);
        File file = new File(gtfsOutLocation + folder);
        if(!file.mkdirs()) {
            throw new RuntimeException("could not create folder for gtfs data");
        }

        //spustim job
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("exportFileLocation", new JobParameter(gtfsOutLocation + folder));
        parameters.put("schema", new JobParameter(schema));

        //TODO joby by mohly bezet asynchronne kazdy v novem vlakne, tak by to trvalo jen tak dlouho, jako nejpomalejsi z nich
        JobExecution execution = jobLauncher.run(gtfsExportAgencyBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportCalendarBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportCalendarDateBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportRouteBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportShapeBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportStopTimeBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportStopBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportTripBatchJob, new JobParameters(parameters));
        TimeTableServiceImpl.failOnJobFailure(execution);

        StreamingResponseBody streamingResponseBody = out -> {
            ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(out));
            File[] files = file.listFiles();
            Assert.notNull(files, "no files were created to export");
            for(File exportFile : files) {
                //prochazim vsechny vygenerovat csv soubory a pridam je do zipu, hned se to take streamuje :)
                InputStream inputStream = new BufferedInputStream(exportFile.toURI().toURL().openStream());
                zip.putNextEntry(new ZipEntry(baseFile.toURI().relativize(exportFile.toURI()).getPath()));

                int length;
                byte[] b = new byte[2048];
                while((length = inputStream.read(b)) > 0) {
                    zip.write(b, 0, length);
                }

                zip.closeEntry();
                inputStream.close();
            }

            zip.close();
        };

        //TODO taky by se hodilo po stazeni ty soubory mazat
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/zip");
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + schema + ".zip");
        return new ResponseEntity<>(streamingResponseBody, httpHeaders, HttpStatus.OK);
    }

    /**
     * Upload multiple file using Spring Controller
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadMultipleFileHandler(@RequestParam("file") MultipartFile file) throws Throwable {
        final String schema = SchemaThreadLocal.get();
        Assert.notNull(schema, "schema must be specified");

        final String folder = schema + "-" + UUID.randomUUID().toString();
        final Set<String> uploadedFiles = new HashSet<>();
        byte[] buffer = new byte[2048];
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(file.getInputStream()));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while(zipEntry != null) {
            String fileName = zipEntry.getName();

            //zmenim nazev slozky, kam se to rozbali. Ocekavam, ze prisel zip archiv slozky, ve ktere jsou hned jizdni rady
            String[] split = fileName.split("/");
            String tmpName;
            if(split.length == 1) {
                tmpName = split[0];
            } else if(split.length == 2) {
                tmpName = split[1];
            } else {
                throw new BadRequestException("bad .zip file");
            }

            //pokud je to file, ktery nechci (neznam), tak skip
            if(!TimeTableServiceImpl.TIME_TABLE_FILES.keySet().contains(tmpName)) {
                zipEntry = zipInputStream.getNextEntry();
                continue;
            }

            uploadedFiles.add(tmpName);
            fileName = folder + "/" + tmpName;

            //vytvorim soubor
            File newFile = new File(gtfsInLocation + fileName);

            //a potrebne slozky, pokud uz neexistuji
            new File(newFile.getParent()).mkdirs();

            //a vlozim do souboru content
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while((len = zipInputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();

            zipEntry = zipInputStream.getNextEntry();
        }

        //zkontroluji, zda jsem nahral vsechny povinne soubory
        for(Map.Entry<String, Boolean> entry: TimeTableServiceImpl.TIME_TABLE_FILES.entrySet()) {
            if(Boolean.TRUE.equals(entry.getValue()) && !uploadedFiles.contains(entry.getKey())) {
                throw new BadRequestException("missing file " + entry.getKey());
            }
        }

        //a zavolam import v novem vlakne
        timeTableService.generateTimeTableToDatabases(schema, gtfsInLocation + folder);

        return new ResponseEntity<>("uploaded", HttpStatus.OK);
    }
}
