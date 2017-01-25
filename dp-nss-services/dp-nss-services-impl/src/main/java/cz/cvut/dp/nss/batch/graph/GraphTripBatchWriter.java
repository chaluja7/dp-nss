package cz.cvut.dp.nss.batch.graph;

import cz.cvut.dp.nss.graph.services.trip.TripNode;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphTripBatchWriter")
public class GraphTripBatchWriter implements ItemWriter<TripNode> {

    @Autowired
    protected TripNodeService tripNodeService;

    @Override
    public void write(List<? extends TripNode> items) throws Exception {
        for(TripNode tripNode : items) {
            //ulozi se jak trip tak navazene (a uz propojene) stopTimes
            tripNodeService.save(tripNode);
        }
    }

}
