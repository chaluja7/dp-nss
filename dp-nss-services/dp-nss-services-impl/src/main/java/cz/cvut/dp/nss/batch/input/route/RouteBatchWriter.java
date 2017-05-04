package cz.cvut.dp.nss.batch.input.route;

import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.route.RouteService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writer importu spoje.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "routeBatchWriter")
public class RouteBatchWriter implements ItemWriter<Route> {

    @Autowired
    private RouteService routeService;

    @Override
    public void write(List<? extends Route> items) throws Exception {
        for(Route route : items) {
            routeService.create(route);
        }
    }

}
