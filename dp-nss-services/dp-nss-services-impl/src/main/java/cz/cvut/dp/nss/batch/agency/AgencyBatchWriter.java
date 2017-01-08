package cz.cvut.dp.nss.batch.agency;

import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.agency.AgencyService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "agencyBatchWriter")
public class AgencyBatchWriter implements ItemWriter<Agency> {

    @Autowired
    protected AgencyService agencyService;

    @Override
    public void write(List<? extends Agency> items) throws Exception {
        for(Agency agency : items) {
            agencyService.create(agency);
        }
    }

}
