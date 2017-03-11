import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AdminStopService} from "../../_service/_admin/admin-stop.service";
import {Stop} from "../../_model/search-result-model";
import {AppSettings} from "../../_common/app.settings";
import {PagerService} from "../../_service/_admin/pager.service";
import {Pager} from "../../_model/pager";

@Component({
  moduleId: module.id,
  selector: 'stops-component',
  templateUrl: './stops.component.html'
})
export class StopsComponent implements OnInit {

  stops: Stop[];

  totalCount: number;

  pager: Pager = new Pager();

  orderColumn: string = 'id';

  orderAsc: boolean = true;

  filter: Stop = new Stop();

  loading: boolean;

  constructor(private router: Router, private adminStopService: AdminStopService, private pagerService: PagerService) {}

  ngOnInit(): void {
    this.setPage(1);
  }

  doFilter() {
    this.setPage(1);
  }

  setPage(page: number) {
    if(page < 1 || page > this.pager.totalPages) {
      return;
    }

    this.loading = true;
    this.pager = this.pagerService.getPager(this.totalCount, page);
    this.adminStopService.getStops(this.filter, this.pager.startIndex ? this.pager.startIndex : 0,
        this.pager.pageSize ? this.pager.pageSize : AppSettings.DEFAULT_PAGE_LIMIT, PagerService.getSortHeaderValue(this.orderColumn, this.orderAsc))
        .subscribe(response => {
          this.totalCount = +response.headers.get(AppSettings.TOTAL_COUNT_HEADER);
          this.stops = response.json();
          this.loading = false;

          this.pager = this.pagerService.getPager(this.totalCount, page);
        }, err  => {});

  }

  goToDetail(stop: Stop): void {
    this.router.navigate(['/stop', stop.id]);
  }

  setOrder(column: string, orderAsc: boolean) {
    this.orderColumn = column;
    this.orderAsc = orderAsc;

    this.setPage(1);
  }

}
