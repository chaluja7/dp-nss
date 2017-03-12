import {Component} from "@angular/core";
import {Pager} from "../../_model/pager";

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

  doFilter() {
    this.setPage(1);
  }

  setOrder(column: string, orderAsc: boolean) {
    this.orderColumn = column;
    this.orderAsc = orderAsc;

    this.setPage(1);
  }

  abstract goToDetail(entity: any): void;

}
