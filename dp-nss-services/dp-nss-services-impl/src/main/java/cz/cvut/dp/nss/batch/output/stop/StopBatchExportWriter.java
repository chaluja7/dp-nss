package cz.cvut.dp.nss.batch.output.stop;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * Writer exportu stanice.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class StopBatchExportWriter extends FlatFileItemWriter {

    public StopBatchExportWriter() {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("stop_id,stop_name,stop_lat,stop_lon,parent_station,wheelchair_boarding"));
    }

}
