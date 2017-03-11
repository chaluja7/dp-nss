import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {Stop} from "../../_model/search-result-model";
import {AdminStopService} from "../../_service/_admin/admin-stop.service";
import {AppSettings} from "../../_common/app.settings";
@Component({
  moduleId: module.id,
  selector: 'stop-component',
  templateUrl: './stop.component.html'
})
export class StopComponent implements OnInit {

  stop: Stop;
  loading = false;
  error = '';
  wheelChairOptions = AppSettings.getPossibleWheelChairOptions();

  constructor(private adminStopService: AdminStopService, private route: ActivatedRoute, private location: Location) {}

  ngOnInit(): void {
    this.route.params
        .switchMap((params: Params) => this.adminStopService.getStop(params['id']))
        .subscribe(stop => {this.stop = stop}, err  => {});
  }

  onSubmit(): void {
    this.loading = true;
    this.adminStopService.update(this.stop)
        .subscribe(timeTable => {this.goBack()},
            err  => {
              this.error = 'Chyba při zpracování';
              this.loading = false;
            });
  }

  goBack(): void {
    this.location.back();
  }

}
