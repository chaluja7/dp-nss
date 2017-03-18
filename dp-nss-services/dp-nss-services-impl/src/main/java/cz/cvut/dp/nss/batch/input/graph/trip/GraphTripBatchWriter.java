package cz.cvut.dp.nss.batch.input.graph.trip;

import cz.cvut.dp.nss.graph.services.trip.TripNode;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
import org.neo4j.ogm.session.Session;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphTripBatchWriter")
public class GraphTripBatchWriter implements ItemWriter<TripNode> {

    @Autowired
    protected TripNodeService tripNodeService;

    @Autowired
    protected Session session;

    @Override
    @Transactional("neo4jTransactionManager")
    public void write(List<? extends TripNode> items) throws Exception {
        //velmi dulezite, jinak postupne dojde k degradaci rychlosti zapisovani
        session.clear();

        for(TripNode tripNode : items) {
            //ulozi se jak trip tak navazene (a uz propojene) stopTimes
            tripNodeService.save(tripNode, -1);
        }
    }

}
