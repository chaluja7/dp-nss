package cz.cvut.dp.nss.services.operationInterval;

import java.util.List;

/**
 * Common interface for all OperationIntervalService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface OperationIntervalService {

    /**
     * find operationInterval by id
     * @param id operationIntervalId
     * @return operationInterval by id
     */
    OperationInterval getOperationInterval(long id);

    /**
     * update operationInterval
     * @param operationInterval operationInterval to update
     * @return updated operationInterval
     */
    OperationInterval updateOperationInterval(OperationInterval operationInterval);

    /**
     * create operationInterval
     * @param operationInterval operationInterval to create
     */
    void createOperationInterval(OperationInterval operationInterval);

    /**
     * delete operationInterval by id
     * @param id operationIntervalId
     */
    void deleteOperationInterval(long id);

    /**
     * @return find all operationIntervals
     */
    List<OperationInterval> getAll();

    /**
     * will import operationInterval to Neo4j database
     * @param operationInterval operationInterval to import
     */
    void importOperationIntervalToNeo4j(OperationInterval operationInterval);

}
