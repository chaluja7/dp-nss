package cz.cvut.dp.nss.batch.input.agency;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DefaultFieldSet;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
public class AgencyBatchReader extends FlatFileItemReader<DefaultFieldSet> {

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

}
