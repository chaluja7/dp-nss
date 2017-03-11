package cz.cvut.dp.nss.controller.admin;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.controller.interceptor.CheckAccess;
import cz.cvut.dp.nss.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jakubchalupa
 * @since 06.03.17
 */
@CheckAccess
public abstract class AdminAbstractController extends AbstractController {

    protected static final String X_LIMIT_HEADER = "X-Limit";

    protected static final String X_OFFSET_HEADER = "X-Offset";

    protected static final String X_ORDER_HEADER = "X-Order";

    protected static final String X_COUNT_HEADER = "X-Total-Count";

    protected static final String FILTER_ID = "id";

    /**
     * Z X-Order hlavicky vytahne pozadovany sloupec a smer razeni. Ocekavana forma napr. 'id:desc'
     * @param xOrderHeader radici hlavicka
     * @return objekt razeni
     * @throws BadRequestException pokud prijde spatny format hlavicky
     */
    protected static OrderWrapper getOrderFromHeader(String xOrderHeader) throws BadRequestException {
        String orderColumn = null;
        boolean asc = true;

        if(xOrderHeader != null) {
            if(xOrderHeader.contains(":")) {
                String[] split = xOrderHeader.split(":");
                if (split.length == 2) {
                    orderColumn = split[0];
                    asc = !"desc".equalsIgnoreCase(split[1]);
                } else {
                    throw new BadRequestException("invalid X-Order header");
                }
            } else {
                orderColumn = xOrderHeader;
            }
        }

        return new OrderWrapper(orderColumn, asc);
    }

    protected static Map<String, String> getFiltersFromHeader(String xFilterHeader) throws BadRequestException {
        Map<String, String> filtersMap = new HashMap<>();

        if(!StringUtils.isBlank(xFilterHeader)) {
            //rozsekam po strednicich na jednotlive filtry
            String[] split;
            if(xFilterHeader.contains(";")) {
                split = xFilterHeader.split(";");
            } else {
                split = new String[1];
                split[0] = xFilterHeader;
            }

            //a kazdy filtr by nyni mel byt ve tvaru field=value
            for(String filter : split) {
                if(filter.contains("=")) {
                    String[] filterSplit = filter.split("=");
                    if(filterSplit.length == 2) {
                        filtersMap.put(filterSplit[0], filterSplit[1]);
                    } else {
                        throw new BadRequestException("invalid X-Filter header");
                    }
                } else {
                    throw new BadRequestException("invalid X-Filter header");
                }
            }
        }

        return filtersMap;
    }

}
