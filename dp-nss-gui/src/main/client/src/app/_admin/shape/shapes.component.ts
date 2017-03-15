import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Route} from "../../_model/search-result-model";
import {AppSettings} from "../../_common/app.settings";
import {PagerService} from "../../_service/_admin/pager.service";
import {AbstractFilteringComponent} from "../filtering.component.ts/abstract-filtering.component";
import {Shapes} from "../../_model/shapes";
import {AdminShapeService} from "../../_service/_admin/admin-shape.service";

@Component({
  moduleId: module.id,
  selector: 'shapes-component',
  templateUrl: './shapes.component.html'
})
export class ShapesComponent extends AbstractFilteringComponent implements OnInit {

  shapes: Shapes[];

  filter: Shapes = new Shapes();

  constructor(private router: Router, private adminShapeService: AdminShapeService, private pagerService: PagerService) {
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
    this.adminShapeService.getShapes(this.filter, this.pager.startIndex ? this.pager.startIndex : 0,
        this.pager.pageSize ? this.pager.pageSize : AppSettings.DEFAULT_PAGE_LIMIT, PagerService.getSortHeaderValue(this.orderColumn, this.orderAsc))
        .subscribe(response => {
          this.totalCount = +response.headers.get(AppSettings.TOTAL_COUNT_HEADER);
          this.shapes = response.json();
          this.loading = false;

          this.pager = this.pagerService.getPager(this.totalCount, page);
        }, err  => {});

  }

  goToDetail(route: Route): void {
    this.router.navigate(['/shape', route.id]);
  }

}
