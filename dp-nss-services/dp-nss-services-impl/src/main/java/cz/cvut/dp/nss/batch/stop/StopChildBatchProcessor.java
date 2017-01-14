package cz.cvut.dp.nss.batch.stop;

import cz.cvut.dp.nss.services.stop.Stop;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "stopChildBatchProcessor")
public class StopChildBatchProcessor implements ItemProcessor<DefaultFieldSet, Stop> {

    @Override
    public Stop process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();
        String parentStopId = (String) properties.get("parent_station");
        if(!StringUtils.hasText(parentStopId)) {
            //nyni nechci ukladat parent stanice, protoze ty by jiz mely byt v db. takto se uz to nedostane dal do writeru
           return null;
        }

        Stop stop = StopParentBatchProcessor.getParentStopFromProperties(properties);

        Stop parentStop = new Stop();
        parentStop.setId(parentStopId);
        stop.setParentStop(parentStop);

        return stop;
    }

}
