package cz.cvut.dp.nss.batch.output.stopTime;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * Writer pro export zastaveni.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class StopTimeBatchExportWriter extends FlatFileItemWriter {

    public StopTimeBatchExportWriter() {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("trip_id,arrival_time,departure_time,stop_id,stop_sequence"));
    }

}
