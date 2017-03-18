package cz.cvut.dp.nss.batch.input;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * @author jakubchalupa
 * @since 14.01.17
 */
public class BatchLineDynamicTokenizer extends DelimitedLineTokenizer implements StepExecutionListener {

    private ExecutionContext jobExecutionContext;

    @Override
    public FieldSet tokenize(String line) {
        if(!hasNames()) {
            //pokud jsem nenastavil jmena sloupcu explicitne, tak musi byt ulozeny v contextu
            //tam se dostaly z prvniho radku csv v BatchHeaderLineCallbackHandleru
            setNames(super.tokenize((String) jobExecutionContext.get(BatchHeaderLineDynamicCallbackHandler.HEADER_LINE_KEY)).getValues());
        }

        return super.tokenize(line);
    }

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

}
