import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Route} from "../../_model/search-result-model";
import {AppSettings} from "../../_common/app.settings";
import {PagerService} from "../../_service/_admin/pager.service";
import {AbstractFilteringComponent} from "../filtering.component.ts/abstract-filtering.component";
import {AdminRouteService} from "../../_service/_admin/admin-route.service";
import {Agency} from "../../_model/agency";
import {AdminAgencyService} from "../../_service/_admin/admin-agency.service";

@Component({
  moduleId: module.id,
  selector: 'routes-component',
  templateUrl: './routes.component.html'
})
export class RoutesComponent extends AbstractFilteringComponent implements OnInit {

  routes: Route[];

  filter: Route = new Route();

  routeTypeOptions = AppSettings.getPossibleRouteTypesOptions();

  agenciesForSelectBox: Agency[];

  constructor(private router: Router, private adminRouteService: AdminRouteService, private pagerService: PagerService,
              private adminAgencyService: AdminAgencyService) {
    super();
  }

  ngOnInit(): void {
    this.setPage(1);
    this.adminAgencyService.getAgenciesForSelectBox()
        .subscribe(agencies => {
          this.agenciesForSelectBox = agencies;
          this.agenciesForSelectBox.unshift(new Agency(null, null));
        }, err => {});
  }

  setPage(page: number) {
    if(page < 1 || (page > 1 && page > this.pager.totalPages)) {
      return;
    }

    this.loading = true;
    this.pager = this.pagerService.getPager(this.totalCount, page);
    this.adminRouteService.getRoutes(this.filter, this.pager.startIndex ? this.pager.startIndex : 0,
        this.pager.pageSize ? this.pager.pageSize : AppSettings.DEFAULT_PAGE_LIMIT, PagerService.getSortHeaderValue(this.orderColumn, this.orderAsc))
        .subscribe(response => {
          this.totalCount = +response.headers.get(AppSettings.TOTAL_COUNT_HEADER);
          this.routes = response.json();
          this.loading = false;

          this.pager = this.pagerService.getPager(this.totalCount, page);
        }, err  => {});

  }

  goToDetail(route: Route): void {
    this.router.navigate(['/route', route.id]);
  }

}
