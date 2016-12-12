package cz.cvut.dp.nss.services.line;

import java.util.List;

/**
 * Common interface for all LineService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface LineService {

    /**
     * find line by id
     * @param id id of a line
     * @return line by id or null
     */
    Line getLine(long id);

    /**
     * update line
     * @param line line to update
     * @return updated line
     */
    Line updateLine(Line line);

    /**
     * persists line
     * @param line line to persist
     */
    void createLine(Line line);

    /**
     * delete line
     * @param id id of line to delete
     */
    void deleteLine(long id);

    /**
     * find all lines
     * @return all lines
     */
    List<Line> getAll();

    /**
     * get count of all lines
     * @return count of all lines
     */
    int getCountAll();

}
