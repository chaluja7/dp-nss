package cz.cvut.dp.nss.batch.graph.stop;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNodeService;
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
@Component(value = "graphStopBatchWriter")
public class GraphStopBatchWriter implements ItemWriter<String> {

    @Autowired
    protected StopTimeNodeService stopTimeNodeService;

    @Autowired
    protected Session session;

    @Override
    @Transactional("neo4jTransactionManager")
    public void write(List<? extends String> items) throws Exception {
        //velmi dulezite, jinak postupne dojde k degradaci rychlosti zapisovani
        session.clear();

        for(String stopName : items) {
            //propojeni uzlu pro tuto stanici (dle nazvu)
            stopTimeNodeService.connectStopTimeNodesOnStopWithWaitingRelationship(stopName);
        }
    }

}
