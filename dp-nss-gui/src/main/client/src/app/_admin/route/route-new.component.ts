import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {AppSettings} from "../../_common/app.settings";
import {AbstractRouteComponent} from "./abstract-route.component";
import {Route} from "../../_model/search-result-model";
@Component({
  moduleId: module.id,
  selector: 'route-new-component',
  templateUrl: './route.component.html'
})
export class RouteNewComponent extends AbstractRouteComponent implements OnInit {

  newRecord = true;

  ngOnInit(): void {
    super.onInit();
    this.routeObject = new Route();
  }

  onSubmit(): void {
    this.loading = true;

    this.adminRouteService.create(this.routeObject)
        .subscribe(route => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR + err;
              this.loading = false;
            });
  }


}
