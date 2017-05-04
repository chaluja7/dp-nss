package cz.cvut.dp.nss.batch.output.calendar;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * Writer pro export intervalu platnosti.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class CalendarBatchExportWriter extends FlatFileItemWriter {

    public CalendarBatchExportWriter() {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date"));
    }

}
