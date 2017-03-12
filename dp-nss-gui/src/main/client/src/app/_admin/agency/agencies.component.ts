import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {PagerService} from "../../_service/_admin/pager.service";
import {AbstractFilteringComponent} from "../filtering.component.ts/abstract-filtering.component";
import {Agency} from "../../_model/agency";
import {AdminAgencyService} from "../../_service/_admin/admin-agency.service";

@Component({
  moduleId: module.id,
  selector: 'agencies-component',
  templateUrl: './agencies.component.html'
})
export class AgenciesComponent extends AbstractFilteringComponent implements OnInit {

  agencies: Agency[];

  filter: Agency = new Agency();

  constructor(private router: Router, private adminAgencyService: AdminAgencyService, private pagerService: PagerService) {
    super();
  }

  ngOnInit(): void {
    this.setPage(1);
  }

  setPage(page: number) {
    if(page < 1 || (page > 1 && page > this.pager.totalPages)) {
      return;
    }

    this.loading = true;
    this.pager = this.pagerService.getPager(this.totalCount, page);
    this.adminAgencyService.getAgencies(this.filter, this.pager.startIndex ? this.pager.startIndex : 0,
        this.pager.pageSize ? this.pager.pageSize : AppSettings.DEFAULT_PAGE_LIMIT, PagerService.getSortHeaderValue(this.orderColumn, this.orderAsc))
        .subscribe(response => {
          this.totalCount = +response.headers.get(AppSettings.TOTAL_COUNT_HEADER);
          this.agencies = response.json();
          this.loading = false;

          this.pager = this.pagerService.getPager(this.totalCount, page);
        }, err  => {});

  }

  goToDetail(agency: Agency): void {
    this.router.navigate(['/agency', agency.id]);
  }

}
