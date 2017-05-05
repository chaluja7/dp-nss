import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {UserService} from "../../_service/user.service";
import {Calendar} from "../../_model/calendar";
import {AdminCalendarService} from "../../_service/_admin/admin-calendar.service";
import {DateService} from "../../_service/date.service";
import {AppSettings} from "../../_common/app.settings";
import {CalendarDate} from "../../_model/calendar-date";

/**
 * Komponenta administrace intervalu platnosti
 */
@Component({
    moduleId: module.id
})
export abstract class AbstractCalendarComponent {

    calendar: Calendar;
    loading = false;
    error = '';
    datesError = '';
    calendarDatesError = '';
    abstract newRecord: boolean;
    exceptionTypeOptions = AppSettings.getPossibleExceptionTypeOptions();

    constructor(protected adminCalendarService: AdminCalendarService, protected route: ActivatedRoute, protected location: Location,
                protected userService: UserService, private dateService: DateService) {
    }

    /**
     * vynuceni onInit na potomku
     */
    abstract ngOnInit(): void;

    /**
     * obsluha odeslani formulare
     */
    abstract onSubmit(): void;

    /**
     * zkontroluje datum od a datum do a vyjimky z intervalu
     * @returns {boolean} true, pokud jsou vyplnene datumy validni, false jinak
     */
    checkDates(): boolean {
        this.datesError = '';
        this.calendarDatesError = '';

        if (!this.isValidDateRange(this.calendar.startDateObject, this.calendar.endDateObject)) {
            this.datesError = 'Data musí tvořit validní interval.';
            this.loading = false;
            return false;
        }

        if (this.calendar.calendarDates && this.calendar.calendarDates.length > 0) {
            for (let c of this.calendar.calendarDates) {
                if (c.dateObject < this.calendar.startDateObject || c.dateObject > this.calendar.endDateObject) {
                    this.calendarDatesError = 'Všechna data výjimek musí patřit do intervalu platnosti.';
                    this.loading = false;
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * prida novou vyjimku do pole
     */
    addNewCalendarDateItem(): void {
        if (!this.calendar.calendarDates) this.calendar.calendarDates = [];

        let c = new CalendarDate();
        c.exceptionType = 1;
        this.calendar.calendarDates.push(c);
    }

    /**
     * smaze vyjimku z indexu pole
     * @param index index vyjimky
     */
    deleteCalendarDateItem(index: number) {
        this.calendar.calendarDates.splice(index, 1);
    }

    /**
     * osetri datumy
     */
    handleDates(): void {
        this.fillStartDateString(this.calendar.startDateObject);
        this.fillEndDateString(this.calendar.endDateObject);

        //a taky musim naplnit vsechny objekty datumu v exception dates
        if (this.calendar.calendarDates && this.calendar.calendarDates.length > 0) {
            for (let c of this.calendar.calendarDates) {
                c.date = DateService.getFormattedDateOnly(c.dateObject);
            }
        }
    }

    /**
     * vlozi startDate do objektu
     * @param date string date
     */
    fillStartDateObject(date: string): void {
        if (date) {
            this.calendar.startDateObject = DateService.getDateObjectFromString(date);
        }
    }

    /**
     * vlozi endDate do objektu
     * @param date string date
     */
    fillEndDateObject(date: string): void {
        if (date) {
            this.calendar.endDateObject = DateService.getDateObjectFromString(date);
        }
    }

    /**
     * vlozi startDate string do objektu
     * @param date objekt date
     */
    fillStartDateString(date: Date): void {
        if (date) {
            this.calendar.startDate = DateService.getFormattedDateOnly(date);
        }
    }

    /**
     * vlozi endDate string do objektu
     * @param date objekt date
     */
    fillEndDateString(date: Date): void {
        if (date) {
            this.calendar.endDate = DateService.getFormattedDateOnly(date);
        }
    }

    /**
     * @param startDate startDate
     * @param endDate endDate
     * @returns {boolean} true, pokud datumy tvori interval, false jinak
     */
    isValidDateRange(startDate: Date, endDate: Date): boolean {
        return startDate <= endDate;
    }

    /**
     * vrati se zpet v historii
     */
    goBack(): void {
        this.location.back();
    }

}
