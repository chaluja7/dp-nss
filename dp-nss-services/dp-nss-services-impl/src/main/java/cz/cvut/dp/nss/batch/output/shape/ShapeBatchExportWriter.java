package cz.cvut.dp.nss.batch.output.shape;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * @author jakubchalupa
 * @since 18.03.17
 */
public class ShapeBatchExportWriter extends FlatFileItemWriter {

    public ShapeBatchExportWriter() {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("shape_id,shape_pt_lat,shape_pt_lon,shape_pt_sequence"));
    }

}
