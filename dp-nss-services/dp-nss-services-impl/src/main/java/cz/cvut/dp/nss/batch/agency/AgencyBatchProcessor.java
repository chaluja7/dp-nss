package cz.cvut.dp.nss.batch.agency;

import cz.cvut.dp.nss.batch.BatchStringUtils;
import cz.cvut.dp.nss.services.agency.Agency;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.UUID;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "agencyBatchProcessor")
public class AgencyBatchProcessor implements ItemProcessor<DefaultFieldSet, Agency> {

    @Override
    public Agency process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        Agency agency = new Agency();
        agency.setName((String) properties.get("name"));
        agency.setUrl((String) properties.get("url"));
        agency.setPhone(BatchStringUtils.notEmptyStringOrNull((String) properties.get("phone")));

        //id je nepovinne (wtf?), ale u nas je to primarni klic, tak musim nejake unikatni vybrat
        String id = (String) properties.get("id");
        if(!StringUtils.hasText(id)) {
            id = "agency-" + UUID.randomUUID().toString();
        }
        agency.setId(id);

        return agency;
    }

}
