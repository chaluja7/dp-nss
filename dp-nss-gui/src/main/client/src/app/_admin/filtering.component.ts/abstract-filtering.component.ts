import {Component} from "@angular/core";
import {Pager} from "../../_model/pager";

/**
 * Predek komponent s umoznenym filtrovanim zaznamu
 */
@Component({
    moduleId: module.id
})
export abstract class AbstractFilteringComponent {

    loading: boolean;

    orderColumn: string = 'id';

    orderAsc: boolean = true;

    pager: Pager = new Pager();

    totalCount: number;

    abstract setPage(page: number);

    /**
     * inicialni filtrovani
     */
    doFilter() {
        this.setPage(1);
    }

    /**
     * nastavi radici sloupec
     * @param column sloupec
     * @param orderAsc true, pokud se ma radit ascending, false jinak
     */
    setOrder(column: string, orderAsc: boolean) {
        this.orderColumn = column;
        this.orderAsc = orderAsc;

        this.setPage(1);
    }

    /**
     * vynuceni metody goToDetail
     * @param entity entita k prechodu na detail
     */
    abstract goToDetail(entity: any): void;

}
