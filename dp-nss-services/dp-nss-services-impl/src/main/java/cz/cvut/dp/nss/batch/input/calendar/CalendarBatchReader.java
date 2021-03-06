package cz.cvut.dp.nss.batch.input.calendar;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.core.io.Resource;

/**
 * Raeder importu intervalu platnosti.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
public class CalendarBatchReader extends FlatFileItemReader<DefaultFieldSet> {

    @Override
    protected DefaultFieldSet doRead() throws Exception {
        return super.doRead();
    }

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();
    }

    @Override
    protected void doClose() throws Exception {
        super.doClose();
    }

    @Override
    public void setResource(Resource resource) {
        super.setResource(resource);
    }

}
