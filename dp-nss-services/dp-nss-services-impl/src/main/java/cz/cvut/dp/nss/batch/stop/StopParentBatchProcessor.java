package cz.cvut.dp.nss.batch.stop;

import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopWheelchairBoardingType;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "stopParentBatchProcessor")
public class StopParentBatchProcessor implements ItemProcessor<DefaultFieldSet, Stop> {

    @Override
    public Stop process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();
        String parentStopId = (String) properties.get("parent_station");
        if(StringUtils.hasText(parentStopId)) {
            //nyni nechci ukladat stanice, ktere maji parent station, protoze parent station jeste nemusi byt v db
            //takto se to uz nedostane dal do writeru
           return null;
        }

        return getParentStopFromProperties(properties);
    }

    static Stop getParentStopFromProperties(Properties properties) {
        Stop stop = new Stop();
        stop.setId((String) properties.get("stop_id"));
        stop.setName((String) properties.get("stop_name"));
        stop.setLat(Double.parseDouble((String) properties.get("stop_lat")));
        stop.setLon(Double.parseDouble((String) properties.get("stop_lon")));

        String wheelchairString = (String) properties.get("wheelchair_boarding");
        if(StringUtils.hasText(wheelchairString)) {
            stop.setStopWheelchairBoardingType(EnumUtils.fromCode(Integer.parseInt(wheelchairString), StopWheelchairBoardingType.class));
        } else {
            stop.setStopWheelchairBoardingType(StopWheelchairBoardingType.NO_INFO);
        }

        return stop;
    }

}
