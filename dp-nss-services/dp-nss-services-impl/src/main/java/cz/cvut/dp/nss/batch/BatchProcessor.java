package cz.cvut.dp.nss.batch;

import cz.cvut.dp.nss.services.agency.Agency;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "batchProcessor")
public class BatchProcessor implements ItemProcessor<DefaultFieldSet, Agency> {

    @Override
    public Agency process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        Agency agency = new Agency();
        agency.setId((String) properties.get("id"));
        agency.setName((String) properties.get("name"));
        agency.setUrl((String) properties.get("url"));
        agency.setPhone((String) properties.get("phone"));

        return agency;
    }

}
