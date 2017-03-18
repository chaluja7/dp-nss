package cz.cvut.dp.nss.batch.input.graph.stop;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNodeService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphStopBatchWriter")
public class GraphStopBatchWriter implements ItemWriter<String> {

    @Autowired
    protected StopTimeNodeService stopTimeNodeService;

    @Override
    public void write(List<? extends String> items) throws Exception {
        for(String stopName : items) {
            //propojeni uzlu pro tuto stanici (dle nazvu)
            stopTimeNodeService.connectStopTimeNodesOnStopWithWaitingRelationship(stopName);
        }
    }

}
