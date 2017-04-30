import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Params} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {AbstractAgencyComponent} from "./abstract-agency.component";
@Component({
  moduleId: module.id,
  selector: 'agency-component',
  templateUrl: './agency.component.html'
})
export class AgencyComponent extends AbstractAgencyComponent implements OnInit {

  newRecord = false;

  ngOnInit(): void {
    this.route.params
        .switchMap((params: Params) => this.adminAgencyService.getAgency(params['id']))
        .subscribe(agency => {this.agency = agency}, err  => {});
  }

  onSubmit(): void {
    this.loading = true;

    this.adminAgencyService.update(this.agency)
        .subscribe(agency => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR + err;
              this.loading = false;
            });
  }

  doDelete(): void {
      this.adminAgencyService.delete(this.agency.id)
          .subscribe(() => {
                  this.userService.setMsg(AppSettings.DELETE_SUCCESS);
                  this.goBack()
              },
            err => {
                this.error = AppSettings.SAVE_ERROR + err;
                this.loading = false;
            });

  }

  confirm(msg: string): boolean {
    return confirm(msg);
  }

}
