package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.repository.stopTime.StopTimeNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
@Service
public class StopTimeNodeServiceImpl extends AbstractNodeService<StopTimeNode, StopTimeNodeRepository> implements StopTimeNodeService {

    @Autowired
    public StopTimeNodeServiceImpl(StopTimeNodeRepository repository) {
        super(repository);
    }

    @Autowired
    protected Session session;

    @Resource(name = "stopTimeNodeServiceImpl")
    private StopTimeNodeService stopTimeNodeService;

    @Override
    public StopTimeNode findByStopTimeId(Long stopTimeId) {
        return repo.findByStopTimeId(stopTimeId).createStopTimeNode();
    }

    @Override
    public List<StopTimeNode> findByStopNameOrderByActionTime(String stopName) {
        return repo.findByStopNameOrderByActionTime(stopName);
    }

    @Override
    public void connectStopTimeNodesOnStopWithWaitingRelationship(String stopName) {
        //schvalne nebezi v transakci kvuli rychlosti
        List<StopTimeNode> stopTimeNodesX = findByStopNameOrderByActionTime(stopName);

        int i = 0;
        StopTimeNode firstStopNode = null;
        StopTimeNode stopNodeFrom = null;
        StopTimeNode stopNodeToSave = null;
        for(StopTimeNode stopNodeTo : stopTimeNodesX) {
            if(i == 0) {
                firstStopNode = stopNodeTo;
                stopNodeToSave = firstStopNode;
            }

            if(stopNodeFrom != null) {
                stopNodeFrom.setNextAwaitingStop(stopNodeTo);
            }

            //propojeni v kruhu (posledni s prvnim)
            if(i != 0 && i == stopTimeNodesX.size() - 1) {
                stopNodeTo.setNextAwaitingStop(firstStopNode);
            }

            //ukladam v batchi po 120
            if(++i % 120 == 0) {
                //velmi dulezite, jinak postupne dojde k degradaci rychlosti zapisovani
                session.clear();
                stopTimeNodeService.save(stopNodeToSave, 120);
                stopNodeToSave = stopNodeTo;
            }

            stopNodeFrom = stopNodeTo;
        }

        //a nakonec ulozim zatim neulozene propojeni
        final int rest = i % 120;
        if(rest != 0) {
            //velmi dulezite, jinak postupne dojde k degradaci rychlosti zapisovani
            session.clear();
            //nyni ukladam o jednu vice, protoze jeste ukladam propojeni posledni->prvni
            stopTimeNodeService.save(stopNodeToSave, rest + 1);
        }

    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void addNewStopTimeToStop(StopTimeNode stopTimeNode) {
        //TODO jakubchalupa
        //nejdriv najdu node, ktery bezprostredne bude predchazet tomu nove vkladanemu

        //nalezenemu nodu nastavim jako next nove vkladany

        //a nove vkladanemu nastavit jako next byvaly next nalezeneho nodu

    }
}
