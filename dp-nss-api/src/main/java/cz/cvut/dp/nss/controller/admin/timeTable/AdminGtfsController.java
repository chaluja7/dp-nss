package cz.cvut.dp.nss.controller.admin.timeTable;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
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
@RestController
@RequestMapping(value = "/admin/gtfs")
public class AdminGtfsController extends AdminAbstractController {

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
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + schema + ".zip");
        return new ResponseEntity<>(streamingResponseBody, httpHeaders, HttpStatus.OK);
    }

    /**
     * Upload multiple file using Spring Controller
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadMultipleFileHandler(@RequestParam("file") MultipartFile file) {

        String message = "aaa";
//        try {
//            byte[] bytes = file.getBytes();
//
//            // Creating the directory to store file
//            String rootPath = System.getProperty("catalina.home");
//            File dir = new File(rootPath + File.separator + "tmpFiles");
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // Create the file on server
//            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
//            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//            stream.write(bytes);
//            stream.close();
//        } catch (Exception e) {
//            return "You failed to upload " + name + " => " + e.getMessage();
//        }
        return message;
    }

}
