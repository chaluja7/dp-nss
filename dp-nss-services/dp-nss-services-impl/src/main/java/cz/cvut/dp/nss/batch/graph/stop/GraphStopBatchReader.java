package cz.cvut.dp.nss.batch.graph.stop;

import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stop.StopServiceImpl;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphStopBatchReader")
@Scope("step")
public class GraphStopBatchReader extends AbstractItemCountingItemStreamItemReader<String> {

    @Autowired
    protected StopService stopService;

    private Iterator<Stop> stops;

    /**
     * chci vracet stanice s unikatnimi jmeny, takze si je nekde budu drzet
     */
    private Set<String> alreadyUsedNames = new HashSet<>();

    public GraphStopBatchReader() {
        this.setName(ClassUtils.getShortName(GraphStopBatchReader.class));
    }

    @Override
    protected String doRead() throws Exception {
        if(stops.hasNext()) {
            Stop stop = stops.next();
            String stopName = StopServiceImpl.getFixedStopName(stop.getName());

            if(!alreadyUsedNames.contains(stopName)) {
                alreadyUsedNames.add(stopName);
                return stopName;
            } else {
                return doRead();
            }

        }

        return null;
    }

    @Override
    protected void doOpen() throws Exception {
        stops = stopService.iteratorOverAllStops();
    }

    @Override
    protected void doClose() throws Exception {
        //empty
    }

}
