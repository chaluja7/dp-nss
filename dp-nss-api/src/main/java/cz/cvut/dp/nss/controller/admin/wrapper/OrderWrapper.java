package cz.cvut.dp.nss.controller.admin.wrapper;

/**
 * @author jakubchalupa
 * @since 09.03.17
 */
public class OrderWrapper {

    private final String orderColumn;

    private final boolean asc;

    public OrderWrapper(String orderColumn, boolean asc) {
        this.orderColumn = orderColumn;
        this.asc = asc;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public boolean isAsc() {
        return asc;
    }
}
