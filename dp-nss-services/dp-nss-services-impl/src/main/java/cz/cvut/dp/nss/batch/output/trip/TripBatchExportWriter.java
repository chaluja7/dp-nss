package cz.cvut.dp.nss.batch.output.trip;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class TripBatchExportWriter extends FlatFileItemWriter {

    public TripBatchExportWriter() {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("route_id,service_id,trip_id,trip_headsign,shape_id,wheelchair_accessible"));
    }

}
