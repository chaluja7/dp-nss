import {Component} from "@angular/core";
import {ActivatedRoute, Params, Router, NavigationEnd} from "@angular/router";
import {Location} from "@angular/common";
import {TripService} from "../../_service/trip.service";

@Component({
  moduleId: module.id,
  selector: 'search-trip-map-component',
  templateUrl: './search-trip-map.component.html'
})
export class SearchTripMapComponent {

    mapCenterLat: number;
    mapCenterLon: number;
    trip: any;
    error = '';

    constructor(protected route: ActivatedRoute, protected location: Location, protected tripService: TripService, protected router: Router) {
        //při změně url voláme nový dotaz na search
        router.events.filter(event => event instanceof NavigationEnd)
            .subscribe((event: NavigationEnd) => {
                this.search();
            });
    }

  search(): void {
      this.trip = null;
      this.error = null;
      this.mapCenterLat = null;
      this.mapCenterLon = null;

      this.route.params
          .switchMap((params: Params) => this.tripService.getTrip(params['schema'], params['id'], true))
          .subscribe(trip => {
                  this.trip = trip;

                  if(trip.shapes) {
                      let center = Math.floor(trip.shapes.length / 2);
                      this.mapCenterLat = trip.shapes[center].lat;
                      this.mapCenterLon = trip.shapes[center].lon;
                  }

              },
              err  => {
                  this.error = 'Chyba při načítání jízdy.';
              });
  }

}
