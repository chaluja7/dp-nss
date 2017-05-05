import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Params} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {AbstractCalendarComponent} from "./abstract-calendar.component";
import {DateService} from "../../_service/date.service";

/**
 * Komponenta editace intervalu platnosti
 */
@Component({
    moduleId: module.id,
    selector: 'calendar-component',
    templateUrl: './calendar.component.html'
})
export class CalendarComponent extends AbstractCalendarComponent implements OnInit {

    newRecord = false;

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.adminCalendarService.getCalendar(params['id']))
            .subscribe(calendar => {
                this.calendar = calendar;
                //naplneni objektu datumu
                this.fillStartDateObject(calendar.startDate);
                this.fillEndDateObject(calendar.endDate);

                //a taky musim naplnit vsechny objekty datumu v exception dates
                if (calendar.calendarDates && calendar.calendarDates.length > 0) {
                    for (let c of calendar.calendarDates) {
                        c.dateObject = DateService.getDateObjectFromString(c.date);
                    }
                }


            }, err => {
            });
    }

    onSubmit(): void {
        this.loading = true;

        if (!this.checkDates()) {
            return;
        }

        this.handleDates();
        this.adminCalendarService.update(this.calendar)
            .subscribe(agency => {
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                    this.goBack()
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                    this.loading = false;
                });
    }

    /**
     * smaze interval
     */
    doDelete(): void {
        this.adminCalendarService.delete(this.calendar.id)
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
