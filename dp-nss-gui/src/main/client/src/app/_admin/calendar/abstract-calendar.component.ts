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
              protected userService: UserService, private dateService: DateService) {}

  abstract ngOnInit(): void;

  abstract onSubmit(): void;

  checkDates(): boolean {
    this.datesError = '';
    this.calendarDatesError = '';

    if(!this.isValidDateRange(this.calendar.startDateObject, this.calendar.endDateObject)) {
      this.datesError = 'Data musí tvořit validní interval.';
      this.loading = false;
      return false;
    }

    if(this.calendar.calendarDates && this.calendar.calendarDates.length > 0) {
      for(let c of this.calendar.calendarDates) {
        if(c.dateObject < this.calendar.startDateObject || c.dateObject > this.calendar.endDateObject) {
          this.calendarDatesError = 'Všechna data výjimek musí patřit do intervalu platnosti.';
          this.loading = false;
          return false;
        }
      }
    }

    return true;
  }

  addNewCalendarDateItem(): void {
    if(!this.calendar.calendarDates) this.calendar.calendarDates = [];

    let c = new CalendarDate();
    c.exceptionType = 1;
    this.calendar.calendarDates.push(c);
  }

  deleteCalendarDateItem(index: number) {
    this.calendar.calendarDates.splice(index, 1);
  }

  handleDates(): void {
    this.fillStartDateString(this.calendar.startDateObject);
    this.fillEndDateString(this.calendar.endDateObject);

    //a taky musim naplnit vsechny objekty datumu v exception dates
    if(this.calendar.calendarDates && this.calendar.calendarDates.length > 0) {
      for(let c of this.calendar.calendarDates) {
        c.date = DateService.getFormattedDateOnly(c.dateObject);
      }
    }
  }

  fillStartDateObject(date: string): void {
    if(date) {
      this.calendar.startDateObject = DateService.getDateObjectFromString(date);
    }
  }

  fillEndDateObject(date: string): void {
    if(date) {
      this.calendar.endDateObject = DateService.getDateObjectFromString(date);
    }
  }

  fillStartDateString(date: Date): void {
    if(date) {
      this.calendar.startDate = DateService.getFormattedDateOnly(date);
    }
  }

  fillEndDateString(date: Date): void {
    if(date) {
      this.calendar.endDate = DateService.getFormattedDateOnly(date);
    }
  }

  isValidDateRange(startDate: Date, endDate: Date): boolean {
    return startDate <= endDate;
  }

  goBack(): void {
    this.location.back();
  }

}
