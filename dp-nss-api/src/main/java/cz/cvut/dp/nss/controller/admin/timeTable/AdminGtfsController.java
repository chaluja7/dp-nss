package cz.cvut.dp.nss.controller.admin.timeTable;

import cz.cvut.dp.nss.controller.AbstractController;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author jakubchalupa
 * @since 17.03.17
 */
@Controller
@RequestMapping(value = "/admin/gtfs")
//TODO zatim je to bez pravoveho osetreni, melo by ale pak extendovat admiAbstractController nebo definovat CheckAccessfla
public class AdminGtfsController extends AbstractController {

    @Value("${gtfs.out.location}")
    private String gtfsOutLocation;

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

    @RequestMapping(value = "/download/{id}")
    public ResponseEntity<StreamingResponseBody> downloadTimeTableGTFS(@PathVariable("id") String timeTableId) throws Throwable {
        final String folder = timeTableId + "-" + UUID.randomUUID().toString();
        File baseFile = new File(gtfsOutLocation);
        File file = new File(gtfsOutLocation + folder);
        if(!file.mkdirs()) {
            throw new RuntimeException("could not create folder for gtfs data");
        }

        //spustim job
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("exportFileLocation", new JobParameter(gtfsOutLocation + folder));
        parameters.put("schema", new JobParameter(timeTableId));

        //TODO joby by mohly bezet asynchronne kazdy v novem vlakne, tak by to trvalo jen tak dlouho, jako nejpomalejsi z nich
        JobExecution execution = jobLauncher.run(gtfsExportAgencyBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportCalendarBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportCalendarDateBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportRouteBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportShapeBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportStopTimeBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportStopBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

        execution = jobLauncher.run(gtfsExportTripBatchJob, new JobParameters(parameters));
        failOnJobFailure(execution);

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
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + timeTableId + ".zip");
        return new ResponseEntity<>(streamingResponseBody, httpHeaders, HttpStatus.OK);
    }

}
