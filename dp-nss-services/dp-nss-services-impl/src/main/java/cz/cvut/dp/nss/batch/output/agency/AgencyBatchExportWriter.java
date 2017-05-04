package cz.cvut.dp.nss.batch.output.agency;

import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * Writer exportu dopravce.
 *
 * @author jakubchalupa
 * @since 18.03.17
 */
public class AgencyBatchExportWriter extends FlatFileItemWriter {

    public AgencyBatchExportWriter() {
        //tvorba zahlavi souboru
        super.setHeaderCallback(writer -> writer.write("agency_id,agency_name,agency_url,agency_phone"));
    }

}
