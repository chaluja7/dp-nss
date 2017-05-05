import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {PagerService} from "../../_service/_admin/pager.service";
import {AbstractFilteringComponent} from "../filtering.component.ts/abstract-filtering.component";
import {Calendar} from "../../_model/calendar";
import {AdminCalendarService} from "../../_service/_admin/admin-calendar.service";
import {DateService} from "../../_service/date.service";

/**
 * Komponenta seznamu intervalu platnosti
 */
@Component({
    moduleId: module.id,
    selector: 'calendars-component',
    templateUrl: './calendars.component.html'
})
export class CalendarsComponent extends AbstractFilteringComponent implements OnInit {

    calendars: Calendar[];

    filter: Calendar = new Calendar();

    constructor(private router: Router, private adminCalendarService: AdminCalendarService, private pagerService: PagerService,
                private dateService: DateService) {
        super();
    }

    ngOnInit(): void {
        this.setPage(1);
    }

    /**
     * nastavi stranku a provede vyhledavani
     * @param page cislo stranky
     */
    setPage(page: number) {
        if (page < 1 || (page > 1 && page > this.pager.totalPages)) {
            return;
        }

        this.loading = true;
        this.pager = this.pagerService.getPager(this.totalCount, page);
        this.adminCalendarService.getCalendars(this.filter, this.pager.startIndex ? this.pager.startIndex : 0,
            this.pager.pageSize ? this.pager.pageSize : AppSettings.DEFAULT_PAGE_LIMIT, PagerService.getSortHeaderValue(this.orderColumn, this.orderAsc))
            .subscribe(response => {
                this.totalCount = +response.headers.get(AppSettings.TOTAL_COUNT_HEADER);
                this.calendars = response.json();
                this.loading = false;

                this.pager = this.pagerService.getPager(this.totalCount, page);
            }, err => {
            });

    }

    /**
     * preskoci na detail intervalu
     * @param calendar interval
     */
    goToDetail(calendar: Calendar): void {
        this.router.navigate(['/calendar', calendar.id]);
    }

}
