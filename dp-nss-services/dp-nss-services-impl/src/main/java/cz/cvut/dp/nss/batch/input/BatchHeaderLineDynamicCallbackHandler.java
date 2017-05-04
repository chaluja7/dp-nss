package cz.cvut.dp.nss.batch.input;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.LineCallbackHandler;


/**
 * Dynamicky callback pro zahlavi csv souboru.
 *
 * @author jakubchalupa
 * @since 14.01.17
 */
public class BatchHeaderLineDynamicCallbackHandler implements LineCallbackHandler, StepExecutionListener {

    private ExecutionContext jobExecutionContext;

    public static final String HEADER_LINE_KEY = "headerLine";

    @Override
    public void handleLine(final String line) {
        //do contextu si ulozim zahlavi csv, coz urcuje jmena sloupcu v csv, na ktere se pote budu dotazovat
        jobExecutionContext.put(HEADER_LINE_KEY, line);
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
