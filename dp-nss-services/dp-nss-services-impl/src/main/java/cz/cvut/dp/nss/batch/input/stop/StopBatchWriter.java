package cz.cvut.dp.nss.batch.input.stop;

import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writer importu stanice.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "stopBatchWriter")
public class StopBatchWriter implements ItemWriter<Stop> {

    @Autowired
    protected StopService stopService;

    @Override
    public void write(List<? extends Stop> items) throws Exception {
        for(Stop stop : items) {
            stopService.create(stop);
        }
    }

}
