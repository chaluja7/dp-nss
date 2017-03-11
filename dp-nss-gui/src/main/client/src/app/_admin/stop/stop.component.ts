import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {Stop} from "../../_model/search-result-model";
import {AdminStopService} from "../../_service/_admin/admin-stop.service";
import {AppSettings} from "../../_common/app.settings";
import {UserService} from "../../_service/user.service";
import {RemoteData, CompleterService} from "ng2-completer";
import {Headers} from "@angular/http";
import {HttpClient} from "../../_service/http-client";
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
  remoteStops: RemoteData;
  stopSearchQuery: string;

  constructor(private adminStopService: AdminStopService, private route: ActivatedRoute, private location: Location,
              private userService: UserService, private completerService: CompleterService, private http: HttpClient) {

      this.remoteStops = completerService.remote(null, 'id,name', 'id,name');
      this.remoteStops.urlFormater(term => {return this.userService.getApiPrefix() + `${AdminStopService.STOP_URL}?searchQuery=${term}`;});
      let headers = new Headers();
      http.createAuthorizationHeader(headers);
      this.remoteStops.headers(headers);
      this.remoteStops.dataField('');
  }

  ngOnInit(): void {
    this.route.params
        .switchMap((params: Params) => this.adminStopService.getStop(params['id']))
        .subscribe(stop => {this.stop = stop}, err  => {});
  }

  onSubmit(): void {
    this.loading = true;
    if(!this.stop.parentStopId) {
        //kvuli tomu, abych tam neposlal undefined
        this.stop.parentStopId = null;
    } else if(this.stop.parentStopId.indexOf(' ') >= 0) {
        //z autocompletu tam vlezlo i jmeno, ale ja chci jen ID
        this.stop.parentStopId = this.stop.parentStopId.split(' ')[0];
    }

    this.adminStopService.update(this.stop)
        .subscribe(timeTable => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR;
              this.loading = false;
            });
  }

  goBack(): void {
    this.location.back();
  }

  doDelete(): void {
      this.adminStopService.delete(this.stop.id)
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
