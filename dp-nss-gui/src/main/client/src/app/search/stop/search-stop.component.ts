import {Component} from "@angular/core";
import {ActivatedRoute, Params, Router, NavigationEnd} from "@angular/router";
import {StopService} from "../../_service/stop.service";
import {Stop} from "../../_model/search-result-model";

@Component({
  moduleId: module.id,
  selector: 'search-stop-component',
  templateUrl: './search-stop.component.html'
})
export class SearchStopComponent {

    stop: Stop;
    error = '';

    constructor(protected route: ActivatedRoute, protected stopService: StopService, protected router: Router) {
        //při změně url voláme nový dotaz na search
        router.events.filter(event => event instanceof NavigationEnd)
            .subscribe((event: NavigationEnd) => {
                this.search();
            });
    }

  search(): void {
      this.error = null;

      this.route.params
          .switchMap((params: Params) => this.stopService.getStop(params['schema'], params['id']))
          .subscribe(stop => {
                this.stop = stop;
              },
              err  => {
                  this.error = 'Chyba při načítání jízdy.';
              });

  }

}