import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Calendar} from "../../_model/calendar";
import {DateService} from "../date.service";

@Injectable()
export class AdminCalendarService {

  static CALENDAR_URL = 'admin/calendar';

  //TODO property startDate a endDate nacpat do startDateObject jako Date - bude to urcite zde???.

  constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) { }

  getCalendars(filter: Calendar, offset: number, limit: number, order: string): Observable<Response> {
    let headers = new Headers();
    headers.append(AppSettings.OFFSET_HEADER, offset + '');
    headers.append(AppSettings.LIMIT_HEADER, limit + '');
    headers.append(AppSettings.ORDER_HEADER, order);

    let params: URLSearchParams = new URLSearchParams();
    if(filter.id) params.set('id', filter.id);
    if(filter.startDateObject) params.set('startDate', DateService.getFormattedDateOnly(filter.startDateObject));
    if(filter.endDateObject) params.set('endDate', DateService.getFormattedDateOnly(filter.endDateObject));

    return this.http
        .get(this.userService.getApiPrefix() + AdminCalendarService.CALENDAR_URL, params, headers)
        .map((response => response))
        .catch(err => this.errorService.handleServerError(err));
  }

  getCalendarsForSelectBox(): Observable<Calendar[]> {
    let params: URLSearchParams = new URLSearchParams();
    params.set('allOptions', 'true');

    return this.http
        .get(this.userService.getApiPrefix() + AdminCalendarService.CALENDAR_URL, params)
        .map((response => response.json() as Calendar[]))
        .catch(err => this.errorService.handleServerError(err));
  }

  getCalendar(id: string): Observable<Calendar> {
    const url = this.userService.getApiPrefix() + `${AdminCalendarService.CALENDAR_URL}/${id}`;
    return this.http.get(url)
        .map((response => response.json() as Calendar))
        .catch(err => this.errorService.handleServerError(err));
  }

  update(calendar: Calendar): Observable<Calendar> {
    const url = this.userService.getApiPrefix() + `${AdminCalendarService.CALENDAR_URL}/${calendar.id}`;
    return this.http.put(url, JSON.stringify(calendar))
        .map((response => response.json() as Calendar))
        .catch(err => this.errorService.handleServerError(err));
  }

  create(calendar: Calendar): Observable<Calendar> {
    const url = this.userService.getApiPrefix() + AdminCalendarService.CALENDAR_URL;
    return this.http.post(url, JSON.stringify(calendar))
        .map((response => response.json() as Calendar))
        .catch(err => this.errorService.handleServerError(err));
  }

  delete(id: string): Observable<void> {
    const url = this.userService.getApiPrefix() + `${AdminCalendarService.CALENDAR_URL}/${id}`;
    return this.http.delete(url)
        .map(() => null)
        .catch(err => this.errorService.handleServerError(err));
  }

}