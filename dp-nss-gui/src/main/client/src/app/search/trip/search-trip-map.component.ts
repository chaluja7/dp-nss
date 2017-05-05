import {Component} from "@angular/core";
import {ActivatedRoute, Params, Router, NavigationEnd} from "@angular/router";
import {Location} from "@angular/common";
import {TripService} from "../../_service/trip.service";

/**
 * Komponenta pro zobrazeni detailu tripu na mape.
 */
@Component({
    moduleId: module.id,
    selector: 'search-trip-map-component',
    templateUrl: './search-trip-map.component.html'
})
export class SearchTripMapComponent {

    mapCenterLat: number;
    mapCenterLon: number;
    firstStopTime = null;
    lastStopTime = null;
    trip: any;

    error = '';

    constructor(protected route: ActivatedRoute, protected location: Location, protected tripService: TripService, protected router: Router) {
        //při změně url voláme nový dotaz na search
        router.events.filter(event => event instanceof NavigationEnd)
            .subscribe((event: NavigationEnd) => {
                this.search();
            });
    }

    /**
     * provede vyhledani detailu tripu dle id jidniho radu a id tripu
     */
    search(): void {
        this.trip = null;
        this.error = null;
        this.mapCenterLat = null;
        this.mapCenterLon = null;

        this.route.params
            .switchMap((params: Params) => this.tripService.getTrip(params['schema'], params['id'], true))
            .subscribe(trip => {
                    this.trip = trip;

                    if (trip.shapes) {
                        let center = Math.floor(trip.shapes.length / 2);
                        this.mapCenterLat = trip.shapes[center].lat;
                        this.mapCenterLon = trip.shapes[center].lon;
                    }

                    if (trip.stopTimes) {
                        for (let stopTime of trip.stopTimes) {
                            stopTime.isOpen = false;
                        }
                    }

                },
                err => {
                    this.error = 'Chyba při načítání jízdy.';
                });

        this.route.queryParams.subscribe((params: Params) => {
            this.firstStopTime = params['firstStopTime'];
            this.lastStopTime = params['lastStopTime'];
        });
    }

    /**
     * @param stopTimeId oznaci zastaveni z parametru jako open pro zobrazeni detailu na mape. Ostatni zavre.
     */
    openSimple(stopTimeId: number) {
        if (this.trip && this.trip.stopTimes && this.trip.stopTimes.length > 0) {
            this.trip.stopTimes.forEach((i) => {
                i.isOpen = i.id === stopTimeId;
            });
        }
    }

}
