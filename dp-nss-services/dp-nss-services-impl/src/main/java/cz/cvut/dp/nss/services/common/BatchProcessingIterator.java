package cz.cvut.dp.nss.services.common;

import java.util.Iterator;
import java.util.List;

/**
 * Predek pro iteratory nad velkymi daty z db (postupne sahaji do db)
 *
 * @author jakubchalupa
 * @since 29.01.17
 */
public abstract class BatchProcessingIterator<T> implements Iterator<T> {

    protected static final int MAX_NUMBER_OF_RESULTS_PER_QUERY = 1000;

    private int numberOfIteration = 0;

    private List<T> entityList;

    protected Iterator<T> entityIterator;

    /**
     * @param list list s prvni varkou dat
     */
    public BatchProcessingIterator(List<T> list) {
        setNextChunk(list);
    }

    @Override
    public boolean hasNext() {
        return entityIterator.hasNext();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

    /**
     * zamyslene pouziti je, ze v tele metody se provola setNextChunk s novou varkou dat
     */
    protected abstract void loadNextChunk();

    /**
     * @return true, pokud je mozne, ze v db jsou jeste dalsi zatim neprojete zaznamy
     */
    protected boolean mayHaveNextChunk() {
        return !(entityList.size() < MAX_NUMBER_OF_RESULTS_PER_QUERY);
    }

    /**
     * @param list list s dalsi varkou dat
     * metoda se postara o spravnou inicializaci promennych iteratoru
     */
    protected void setNextChunk(List<T> list) {
        entityList = list;
        entityIterator = list.iterator();
        numberOfIteration++;
    }

    protected int getStart() {
        return numberOfIteration * MAX_NUMBER_OF_RESULTS_PER_QUERY;
    }

    protected int getLimit() {
        return MAX_NUMBER_OF_RESULTS_PER_QUERY;
    }
}
