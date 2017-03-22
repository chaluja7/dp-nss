package cz.cvut.dp.nss.batch.output;

import cz.cvut.dp.nss.batch.BatchStringUtils;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.util.ObjectUtils;

/**
 * Vlastni implementace agregatoru pro spravny zapis do csv - zejma hodnot, ktere obsahuji carku
 *
 * @author jakubchalupa
 * @since 22.03.17
 */
public class BatchDelimitedLineAggregator<T> extends DelimitedLineAggregator<T> {

    private String delimiter;

    @Override
    public String doAggregate(Object[] fields) {
        if(ObjectUtils.isEmpty(fields)) {
            return "";
        } else if(fields.length == 1) {
            return BatchStringUtils.getCsvStringValue(fields[0]);
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < fields.length; ++i) {
                if(i > 0) {
                    sb.append(delimiter);
                }

                sb.append(BatchStringUtils.getCsvStringValue(fields[i]));
            }

            return sb.toString();
        }
    }

    @Override
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        super.setDelimiter(delimiter);
    }
}
