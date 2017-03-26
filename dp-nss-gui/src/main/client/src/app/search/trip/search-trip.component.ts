import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {TripService} from "../../_service/trip.service";

@Component({
  moduleId: module.id,
  selector: 'search-trip-component',
  templateUrl: './search-trip.component.html'
})
export class SearchTripComponent implements OnInit {

    trip: any;
    includeDays: string[];
    excludeDays: string[];
    firstStopTime: number;
    lastStopTime: number;

    error = '';

    constructor(protected route: ActivatedRoute, protected location: Location, protected tripService: TripService) {

    }

  ngOnInit(): void {
      this.includeDays = [];
      this.excludeDays = [];

      this.route.params
          .switchMap((params: Params) => this.tripService.getTrip('pid', params['id']))
          .subscribe(trip => {
              this.trip = trip;
              if(trip.calendar && trip.calendar.calendarDates && trip.calendar.calendarDates.length > 0) {
                  for(let c of trip.calendar.calendarDates) {
                      if(c.exceptionType === 1) {
                          this.includeDays.push(c.date);
                      } else if(c.exceptionType === 2) {
                          this.excludeDays.push(c.date);
                      }
                  }
              }
          },
              err  => {
                  this.error = 'Chyba při načítání jízdy.';
              });

      this.route.queryParams.subscribe((params: Params) => {
          this.firstStopTime = params['firstStopTime'];
          this.lastStopTime = params['lastStopTime'];
      });
  }

  getStopTimeFontWeight(stopTimeId: number): string{
      return stopTimeId == this.firstStopTime || stopTimeId == this.lastStopTime ? 'bold' : 'normal';
  }

  getTripCalendarDays(calendar: any): string {
      let ret = '';
      if(calendar.monday) {
          ret += 'Po | ';
      }
      if(calendar.tuesday) {
          ret += 'Út | ';
      }
      if(calendar.wednesday) {
          ret += 'St | ';
      }
      if(calendar.thursday) {
          ret += 'Čt | ';
      }
      if(calendar.friday) {
          ret += 'Pá | ';
      }
      if(calendar.saturday) {
          ret += 'So | ';
      }
      if(calendar.sunday) {
          ret += 'Ne | ';
      }

      if(ret.endsWith(' | ')) {
          ret = ret.substring(0, ret.length - 3);
      }

      return ret;
  }

}
