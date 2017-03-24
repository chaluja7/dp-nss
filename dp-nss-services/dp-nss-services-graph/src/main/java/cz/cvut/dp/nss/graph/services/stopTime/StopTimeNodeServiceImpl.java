package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.repository.stopTime.StopTimeNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import cz.cvut.dp.nss.graph.services.common.StopTimeQueryResult;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
    @Transactional("neo4jTransactionManager")
    public StopTimeNode findByStopTimeId(Long stopTimeId) {
        return repo.findByStopTimeId(stopTimeId).createStopTimeNode();
    }

    @Override
    @Transactional("neo4jTransactionManager")
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
                stopTimeNodeService.save(stopNodeToSave, 120, true);
                stopNodeToSave = stopNodeTo;
            }

            stopNodeFrom = stopNodeTo;
        }

        //a nakonec ulozim zatim neulozene propojeni
        final int rest = i % 120;
        if(rest != 0) {
            //nyni ukladam o jednu vice, protoze jeste ukladam propojeni posledni->prvni
            stopTimeNodeService.save(stopNodeToSave, rest + 1, true);
        }

    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void addNewStopTimeToStop(StopTimeNode stopTimeNode) {
        //zjistim rozhodujici cas uzlu
        Integer time = stopTimeNode.getDepartureInSeconds() != null ? stopTimeNode.getDepartureInSeconds() : stopTimeNode.getArrivalInSeconds();
        Assert.notNull(time, "vkladany uzel musi mit minimalne jeden z casu arrival/departure");

        //najdu node, ktery bezprostredne bude predchazet tomu nove vkladanemu
        StopTimeNode stopTimeToBreak = findFirstByStopNameAndTimeBefore(stopTimeNode.getStopName(), time);
        if(stopTimeToBreak != null) {
            //nove vkladanemu nastavit jako next byvaly next nalezeneho nodu
            stopTimeNode.setNextAwaitingStop(stopTimeToBreak.getNextAwaitingStop());

            //a nalezenemu nodu nastavim jako next nove vkladany
            //ale nejdriv smazu puvodni nextAwaitingStop
            repo.deleteNextAwaitingStopRelation(stopTimeToBreak.getId());

            stopTimeToBreak.setNextAwaitingStop(stopTimeNode);
            //a ulozim prvni v rade s hloubkou 2 aby se ulozily vsechny zmeny
            save(stopTimeToBreak, 2);
        } else {
            //v db jeste zadny stoptime k dane stanici nebyl, vkladam to tedy jako prvni :)
            //nastavim nextAwaiting stop na ten stejny, aby to tvorilo nutny kruh
            stopTimeNode.setNextAwaitingStop(stopTimeNode);
            save(stopTimeNode, 1);
        }
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public StopTimeNode findFirstByStopNameAndTimeBefore(String stopName, Integer time) {
        StopTimeQueryResult firstByStopNameAndTimeBefore = repo.findFirstByStopNameAndTimeBefore(stopName, time);
        if(firstByStopNameAndTimeBefore == null) {
            //pokud jsem zadny uzel nenasel tak je mozne, ze zadny uzel s mensim casem neni
            //v tom pripade se pokusim najit uzel na dane stanici, ktery je v ramci dne uplne posledni
            firstByStopNameAndTimeBefore = repo.findLastByStopName(stopName);
        }

        return firstByStopNameAndTimeBefore != null ? firstByStopNameAndTimeBefore.createStopTimeNode() : null;
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void deleteStopTimesByTripId(String tripId) {
        //nejdriv najdu vsechny stopTimes s danym tripId. Pro kazdou vezmu prevawaitingstop a nextawaitingstop
        //a ty navzajem propojim (obejdu tak mazany node)
        List<StopTimeQueryResult> stopTimesToDeleteByTripId = repo.findStopTimesToDeleteByTripId(tripId);
        for(StopTimeQueryResult stopTimeQueryResult : stopTimesToDeleteByTripId) {
            StopTimeNode stopTimeNode = stopTimeQueryResult.getPrevAwaitingStop();
            stopTimeNode.setNextAwaitingStop(stopTimeQueryResult.getNextAwaitingStop());
            save(stopTimeNode, 1);
        }

        //a vsechny nody s danym tripId pak smazu
        repo.deleteStopTimesByTripId(tripId);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public int deleteChunk() {
        int count = repo.chunkDeleteAll();
        session.clear();

        return count;
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public StopTimeNode save(StopTimeNode node, int depth, boolean performSessionClear) {
        if(performSessionClear) session.clear();
        return super.save(node, depth);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void changeWheelChairOnStop(String stopId, boolean stopIsWheelChairAccessible) {
        repo.updateWheelChairFlagOnStop(stopId, stopIsWheelChairAccessible);
    }

    @Override
    public void deleteAll() {
        //schvalne neni transactional ale odmazava se to postupne (protoze potvrzeni transakce, kde se smaze vsechno vubec nemusi dobehnout)
        //dle https://neo4j.com/developer/kb/large-delete-transaction-best-practices-in-neo4j/
        int count;
        do {
            count = stopTimeNodeService.deleteChunk();
        } while (count > 0);
    }

}
