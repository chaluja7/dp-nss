import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {AppSettings} from "../../_common/app.settings";
import {UserService} from "../../_service/user.service";
import {RemoteData, CompleterService} from "ng2-completer";
import {Headers} from "@angular/http";
import {HttpClient} from "../../_service/http-client";
import {Trip} from "../../_model/trip";
import {AdminTripService} from "../../_service/_admin/admin-trip.service";
import {AdminRouteService} from "../../_service/_admin/admin-route.service";
import {Calendar} from "../../_model/calendar";
import {AdminCalendarService} from "../../_service/_admin/admin-calendar.service";
import {AdminShapeService} from "../../_service/_admin/admin-shape.service";
import {StopTime} from "../../_model/stopTime";
import {DateService} from "../../_service/date.service";
@Component({
  moduleId: module.id
})
export abstract class AbstractTripComponent {

  trip: Trip;
  loading = false;
  error = '';
  stopTimesError = '';
  wheelChairOptions = AppSettings.getPossibleWheelChairOptions();
  remoteRoutes: RemoteData;
  remoteShapes: RemoteData;
  routesSearchQuery: string;
  abstract newRecord: boolean;
  calendarsForSelectBox: Calendar[];

  constructor(protected adminTripService: AdminTripService, protected route: ActivatedRoute, protected location: Location,
              protected userService: UserService, protected completerService: CompleterService, protected http: HttpClient,
                protected adminCalendarService: AdminCalendarService, protected dateService: DateService) {

      let headers = new Headers();
      http.createAuthorizationHeader(headers);

      this.remoteRoutes = completerService.remote(null, 'id,shortName', 'id,shortName');
      this.remoteRoutes.urlFormater(term => {return encodeURI(this.userService.getApiPrefix() + `${AdminRouteService.ROUTE_URL}?searchQuery=${term}`)});
      this.remoteRoutes.headers(headers);
      this.remoteRoutes.dataField('');

      this.remoteShapes = completerService.remote(null, 'id', 'id');
      this.remoteShapes.urlFormater(term => {return encodeURI(this.userService.getApiPrefix() + `${AdminShapeService.SHAPE_URL}?searchQuery=${term}`)});
      this.remoteShapes.headers(headers);
      this.remoteShapes.dataField('');
  }

    //musi zavolat extendujici trida!
  onInit() {
    this.adminCalendarService.getCalendarsForSelectBox()
        .subscribe(calendars => {
            this.calendarsForSelectBox = calendars;
        }, err => {});
  }

  abstract ngOnInit(): void;

  abstract onSubmit(): void;

  goBack(): void {
    this.location.back();
  }

  protected handleRoute(): void {
    if(!this.trip.routeId) {
          //kvuli tomu, abych tam neposlal undefined
        this.trip.routeId = null;
    } else if(this.trip.routeId.indexOf(' ') >= 0) {
          //z autocompletu tam vlezlo i jmeno, ale ja chci jen ID
        this.trip.routeId = this.trip.routeId.split(' ')[0];
    }
  }

  protected checkTimes(): boolean {
      this.stopTimesError = '';

      if(this.trip.stopTimes && this.trip.stopTimes.length > 0) {
          for(let i = 0; i < this.trip.stopTimes.length; i++) {
            let st = this.trip.stopTimes[i];
            if(st.arrivalObject && st.departureObject && st.arrivalObject > st.departureObject) {
                this.stopTimesError = 'Čas příjezdu do stanice musí být menší nebo roven času odjezdu ze stanice.';
                this.loading = false;
                return false;
            }
          }
      }

      return true;
  }

  protected handleTimes(): void {
      //musim naplnit vsechny objekty casu v stopTimes
      if(this.trip.stopTimes && this.trip.stopTimes.length > 0) {
          for(let st of this.trip.stopTimes) {
              st.arrival = DateService.getFormattedTimeOnly(st.arrivalObject);
              st.departure = DateService.getFormattedTimeOnly(st.departureObject);
          }
      }
  }

    //neni mozne pouzit jednoduchy splice, protoze je treba deep copy
    addNewStopTime(index: number): void {
        if(!this.trip.stopTimes) this.trip.stopTimes = [];

        let arr : StopTime[] = [];
        for(let i = 0; i < index; i++) {
            arr[i] = new StopTime(this.trip.stopTimes[i]);
            arr[i].sequence = i + 1;
        }


        arr[index] = new StopTime();
        arr[index].sequence = index + 1;

        for(let i = index; i < this.trip.stopTimes.length; i++) {
            arr[i + 1] = new StopTime(this.trip.stopTimes[i]);
            arr[i + 1].sequence = i + 2;
        }

        this.trip.stopTimes = arr;
    }

    deleteStopTime(index: number) {
        this.trip.stopTimes.splice(index, 1);

        //a od indexu snizim vsechny sequence o 1
        for(let i = index; i < this.trip.stopTimes.length; i++) {
            this.trip.stopTimes[i].sequence = this.trip.stopTimes[i].sequence - 1;
        }
    }

    confirm(msg: string): boolean {
        return confirm(msg);
    }

}
