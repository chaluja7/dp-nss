import {Component} from "@angular/core";
import {ActivatedRoute, Params, Router, NavigationEnd} from "@angular/router";
import {Location} from "@angular/common";
import {TripService} from "../../_service/trip.service";

/**
 * Komponenta pro zobrazeni detailu jizdy.
 */
@Component({
    moduleId: module.id,
    selector: 'search-trip-component',
    templateUrl: './search-trip.component.html'
})
export class SearchTripComponent {

    schema: string;
    trip: any;
    includeDays: string[];
    excludeDays: string[];
    firstStopTime: number;
    lastStopTime: number;

    error = '';

    constructor(protected route: ActivatedRoute, protected location: Location, protected tripService: TripService, protected router: Router) {
        //při změně url voláme nový dotaz na search
        router.events.filter(event => event instanceof NavigationEnd)
            .subscribe((event: NavigationEnd) => {
                this.search();
            });
    }

    /**
     * zavola REST api pro vytazeni jizdniho radu dle schematu a id
     */
    search(): void {
        this.includeDays = [];
        this.excludeDays = [];
        this.trip = null;
        this.firstStopTime = null;
        this.lastStopTime = null;
        this.error = null;

        this.route.params
            .switchMap((params: Params) => this.tripService.getTrip(params['schema'], params['id'], false))
            .subscribe(trip => {
                    this.trip = trip;
                    if (trip.calendar && trip.calendar.calendarDates && trip.calendar.calendarDates.length > 0) {
                        for (let c of trip.calendar.calendarDates) {
                            if (c.exceptionType === 1) {
                                this.includeDays.push(c.date);
                            } else if (c.exceptionType === 2) {
                                this.excludeDays.push(c.date);
                            }
                        }
                    }
                },
                err => {
                    this.error = 'Chyba při načítání jízdy.';
                });

        this.route.params.subscribe((params: Params) => {
            this.schema = params['schema'];
        });
        this.route.queryParams.subscribe((params: Params) => {
            this.firstStopTime = params['firstStopTime'];
            this.lastStopTime = params['lastStopTime'];
        });
    }

    /**
     * @param stopTimeId id zastaveni
     * @returns {string|string} 'bold' pokud ma byt zastaveni zvyrazneno, 'normal' jinak
     */
    getStopTimeFontWeight(stopTimeId: number): string {
        return stopTimeId == this.firstStopTime || stopTimeId == this.lastStopTime ? 'bold' : 'normal';
    }

    /**
     * @param calendar objekt calendar
     * @returns {string} retezec se dny v tydnu, ve kterych je calendar platny
     */
    getTripCalendarDays(calendar: any): string {
        let ret = '';
        if (calendar.monday) {
            ret += 'Po | ';
        }
        if (calendar.tuesday) {
            ret += 'Út | ';
        }
        if (calendar.wednesday) {
            ret += 'St | ';
        }
        if (calendar.thursday) {
            ret += 'Čt | ';
        }
        if (calendar.friday) {
            ret += 'Pá | ';
        }
        if (calendar.saturday) {
            ret += 'So | ';
        }
        if (calendar.sunday) {
            ret += 'Ne | ';
        }

        if (ret.endsWith(' | ')) {
            ret = ret.substring(0, ret.length - 3);
        }

        return ret;
    }

}
