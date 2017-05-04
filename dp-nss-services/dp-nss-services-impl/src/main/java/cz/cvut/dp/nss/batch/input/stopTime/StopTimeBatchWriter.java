package cz.cvut.dp.nss.batch.input.stopTime;

import cz.cvut.dp.nss.services.stopTime.StopTime;
import cz.cvut.dp.nss.services.stopTime.StopTimeService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writer importu zastaveni.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "stopTimeBatchWriter")
public class StopTimeBatchWriter implements ItemWriter<StopTime> {

    @Autowired
    private StopTimeService stopTimeService;

    @Override
    public void write(List<? extends StopTime> items) throws Exception {
        for(StopTime stopTime : items) {
            stopTimeService.create(stopTime);
        }
    }

}
