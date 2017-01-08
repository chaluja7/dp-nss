package cz.cvut.dp.nss.batch.stop;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.core.io.Resource;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
public class StopBatchReader extends FlatFileItemReader<DefaultFieldSet> {

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
