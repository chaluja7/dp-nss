import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Stop} from "../../_model/search-result-model";
import {AppSettings} from "../../_common/app.settings";
import {PagerService} from "../../_service/_admin/pager.service";
import {AbstractFilteringComponent} from "../filtering.component.ts/abstract-filtering.component";
import {Trip} from "../../_model/trip";
import {AdminTripService} from "../../_service/_admin/admin-trip.service";

/**
 * Komponenta seznamu jizd
 */
@Component({
    moduleId: module.id,
    selector: 'trips-component',
    templateUrl: './trips.component.html'
})
export class TripsComponent extends AbstractFilteringComponent implements OnInit {

    trips: Trip[];

    filter: Trip = new Trip();

    wheelChairOptions = AppSettings.getPossibleWheelChairOptions();

    constructor(private router: Router, private adminTripService: AdminTripService, private pagerService: PagerService) {
        super();
    }

    ngOnInit(): void {
        this.setPage(1);
    }

    /**
     * nastavi stranku a provede vyhledavani jizd
     * @param page cislo stranky
     */
    setPage(page: number) {
        if (page < 1 || (page > 1 && page > this.pager.totalPages)) {
            return;
        }

        this.loading = true;
        this.pager = this.pagerService.getPager(this.totalCount, page);
        this.adminTripService.getTrips(this.filter, this.pager.startIndex ? this.pager.startIndex : 0,
            this.pager.pageSize ? this.pager.pageSize : AppSettings.DEFAULT_PAGE_LIMIT, PagerService.getSortHeaderValue(this.orderColumn, this.orderAsc))
            .subscribe(response => {
                this.totalCount = +response.headers.get(AppSettings.TOTAL_COUNT_HEADER);
                this.trips = response.json();
                this.loading = false;

                this.pager = this.pagerService.getPager(this.totalCount, page);
            }, err => {
            });

    }

    /**
     * preskoci na detail jizdy
     * @param stop jizda
     */
    goToDetail(stop: Stop): void {
        this.router.navigate(['/trip', stop.id]);
    }

}
