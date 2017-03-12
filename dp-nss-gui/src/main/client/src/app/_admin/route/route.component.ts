import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Params} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {AbstractRouteComponent} from "./abstract-route.component";
@Component({
  moduleId: module.id,
  selector: 'route-component',
  templateUrl: './route.component.html'
})
export class RouteComponent extends AbstractRouteComponent implements OnInit {

  newRecord = false;

  ngOnInit(): void {
    super.onInit();
    this.route.params
        .switchMap((params: Params) => this.adminRouteService.getRoute(params['id']))
        .subscribe(route => {
            this.routeObject = route;
        }, err  => {});
  }

  onSubmit(): void {
    this.loading = true;

    this.adminRouteService.update(this.routeObject)
        .subscribe(route => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR;
              this.loading = false;
            });
  }

  doDelete(): void {
      this.adminRouteService.delete(this.routeObject.id)
          .subscribe(() => {
                  this.userService.setMsg(AppSettings.DELETE_SUCCESS);
                  this.goBack()
              },
            err => {
                this.error = AppSettings.SAVE_ERROR;
                this.loading = false;
            });

  }

  confirm(msg: string): boolean {
    return confirm(msg);
  }

}
