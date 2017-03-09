package cz.cvut.dp.nss.controller.admin;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.controller.interceptor.CheckAccess;
import cz.cvut.dp.nss.exception.BadRequestException;

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

}
