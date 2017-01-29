package cz.cvut.dp.nss.batch.graph.stop;

import cz.cvut.dp.nss.services.stop.Stop;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphStopBatchProcessor")
public class GraphStopBatchProcessor implements ItemProcessor<Stop, String> {

    @Override
    public String process(Stop stop) throws Exception {
        return stop.getName();
    }

}
