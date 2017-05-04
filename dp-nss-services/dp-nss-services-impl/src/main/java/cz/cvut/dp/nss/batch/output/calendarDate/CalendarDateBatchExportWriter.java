package cz.cvut.dp.nss.batch.output.calendarDate;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * Writer exportu vyjimky z intervalu platnosti.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class CalendarDateBatchExportWriter extends FlatFileItemWriter {

    public CalendarDateBatchExportWriter() {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("service_id,date,exception_type"));
    }

}
