import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {AppSettings} from "../../_common/app.settings";
import {AbstractCalendarComponent} from "./abstract-calendar.component";
import {Calendar} from "../../_model/calendar";
@Component({
  moduleId: module.id,
  selector: 'calendar-new-component',
  templateUrl: './calendar.component.html'
})
export class CalendarNewComponent extends AbstractCalendarComponent implements OnInit {

  newRecord = true;

  ngOnInit(): void {
      this.calendar = new Calendar();
  }

  onSubmit(): void {
    this.loading = true;

    if(!this.checkDates()) {
      return;
    }

    this.handleDates();
    this.adminCalendarService.create(this.calendar)
        .subscribe(agency => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR;
              this.loading = false;
            });
  }

}
