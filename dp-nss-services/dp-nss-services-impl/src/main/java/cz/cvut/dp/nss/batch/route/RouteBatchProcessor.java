package cz.cvut.dp.nss.batch.route;

import cz.cvut.dp.nss.batch.BatchStringUtils;
import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.route.RouteType;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "routeBatchProcessor")
public class RouteBatchProcessor implements ItemProcessor<DefaultFieldSet, Route> {

    @Override
    public Route process(DefaultFieldSet defaultFieldSet) throws Exception {
        Properties properties = defaultFieldSet.getProperties();

        Route route = new Route();
        route.setId((String) properties.get("route_id"));
        route.setShortName((String) properties.get("route_short_name"));
        route.setLongName(BatchStringUtils.notEmptyStringOrNull((String) properties.get("route_long_name")));
        if(!StringUtils.hasText(route.getShortName())) {
            //short name nemusi byt vyplnene, pokud je vyplnene long name
            route.setShortName(route.getLongName());
        }
        route.setColor(BatchStringUtils.notEmptyStringOrNull((String) properties.get("route_color")));
        route.setRouteType(EnumUtils.fromCode(Integer.parseInt((String) properties.get("route_type")), RouteType.class));

        //agencyId nemusi byt prirazeno
        String agencyId = (String) properties.get("agency_id");
        if(StringUtils.hasText(agencyId)) {
            //vytvorim si virtualni agency, ktere priradim pouze jeji id. ta uz musi existovat v DB!
            Agency agency = new Agency();
            agency.setId(agencyId);
            route.setAgency(agency);
        }

        return route;
    }

}
