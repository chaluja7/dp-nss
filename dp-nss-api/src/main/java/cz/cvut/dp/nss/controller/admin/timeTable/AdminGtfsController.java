package cz.cvut.dp.nss.controller.admin.timeTable;

import cz.cvut.dp.nss.controller.AbstractController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedOutputStream;
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

    @RequestMapping(value = "/download/{id}")
    public ResponseEntity<StreamingResponseBody> downloadTimeTableGTFS(@PathVariable("id") String timeTableId) {
        //TODO - idea je: export z db do gtfs udelat pres batch joby, viz: http://www.mkyong.com/tutorials/spring-batch-tutorial/
        //TODO - csv ukladat do predem urceneho uloziste, napr. /tmp/out
        //TODO - zde jeste vytvorim slozku s new UUID jmenem, to predam jobu jako parametr
        //TODO po dobehnuti jobu zde zacnu soubor cist a hned streamovat do outputstreamu
        //TODO nakonec slozku s csv soubory smazu

        StreamingResponseBody streamingResponseBody = out -> {

            ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(out));

            for(int i = 0; i < 5; i++) {
                zip.putNextEntry(new ZipEntry("entry" + i + ".txt"));

                for(int j = 0; j < 1000000; j++) {
                    zip.write((Integer.toString(j) + " - ").getBytes());
                }

                zip.closeEntry();
            }

            zip.close();
        };

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/zip");
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + timeTableId + ".zip");
        return new ResponseEntity<>(streamingResponseBody, httpHeaders, HttpStatus.OK);
    }

}
