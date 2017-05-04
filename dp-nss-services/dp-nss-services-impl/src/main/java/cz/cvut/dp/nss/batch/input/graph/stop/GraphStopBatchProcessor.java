package cz.cvut.dp.nss.batch.input.graph.stop;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Processor importu stanice do grafu.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphStopBatchProcessor")
public class GraphStopBatchProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String stopName) throws Exception {
        return stopName;
    }

}
