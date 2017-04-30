import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Params} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {AbstractTripComponent} from "./abstract-trip.component";
import {DateService} from "../../_service/date.service";
@Component({
  moduleId: module.id,
  selector: 'trip-component',
  templateUrl: './trip.component.html'
})
export class TripComponent extends AbstractTripComponent implements OnInit {

  newRecord = false;

  ngOnInit(): void {
    super.onInit();

    this.route.params
        .switchMap((params: Params) => this.adminTripService.getTrip(params['id']))
        .subscribe(trip => {
            this.trip = trip;

            //a taky musim naplnit vsechny objekty casu v stopTimes
            if(trip.stopTimes && trip.stopTimes.length > 0) {
                for(let st of trip.stopTimes) {
                    st.arrivalObject = DateService.getTimeObjectFromString(st.arrival);
                    st.departureObject = DateService.getTimeObjectFromString(st.departure);
                }
            }
        }, err  => {});
  }

  onSubmit(): void {
    this.loading = true;
    this.handleRoute();
    this.handleTimes();

    if(!this.checkTimes()) {
        return;
    }

    this.adminTripService.update(this.trip)
        .subscribe(trip => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR + err;
              this.loading = false;
            });
  }

  doDelete(): void {
      this.adminTripService.delete(this.trip.id)
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
