package cz.cvut.dp.nss.batch.output.route;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class RouteBatchExportWriter extends FlatFileItemWriter {

    public RouteBatchExportWriter () {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("route_id,agency_id,route_short_name,route_long_name,route_type,route_color"));
    }

}
