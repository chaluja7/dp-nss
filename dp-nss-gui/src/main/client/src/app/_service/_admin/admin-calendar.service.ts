import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Calendar} from "../../_model/calendar";
import {DateService} from "../date.service";

/**
 * Obsluha administrace intervalu plastnosti
 */
@Injectable()
export class AdminCalendarService {

    static CALENDAR_URL = 'admin/calendar';

    constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) {
    }

    /**
     * vrati intervaly plastnosti ze serveru
     * @param filter filter
     * @param offset odsazeni
     * @param limit limit
     * @param order poradi
     * @returns {Observable<Response>} observable s odpovedi ze serveru
     */
    getCalendars(filter: Calendar, offset: number, limit: number, order: string): Observable<Response> {
        let headers = new Headers();
        headers.append(AppSettings.OFFSET_HEADER, offset + '');
        headers.append(AppSettings.LIMIT_HEADER, limit + '');
        headers.append(AppSettings.ORDER_HEADER, order);

        let params: URLSearchParams = new URLSearchParams();
        if (filter.id) params.set('id', filter.id);
        if (filter.startDateObject) params.set('startDate', DateService.getFormattedDateOnly(filter.startDateObject));
        if (filter.endDateObject) params.set('endDate', DateService.getFormattedDateOnly(filter.endDateObject));

        return this.http
            .get(this.userService.getApiPrefix() + AdminCalendarService.CALENDAR_URL, params, headers)
            .map((response => response))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati intervaly platnosti pro ciselnik
     * @returns {Observable<Calendar[]>} observable s odpovedi ze serveru
     */
    getCalendarsForSelectBox(): Observable<Calendar[]> {
        let params: URLSearchParams = new URLSearchParams();
        params.set('allOptions', 'true');

        return this.http
            .get(this.userService.getApiPrefix() + AdminCalendarService.CALENDAR_URL, params)
            .map((response => response.json() as Calendar[]))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati interval platnosti ze serveru
     * @param id id intervalu
     * @returns {Observable<Calendar>} observable s odpovedi ze serveru
     */
    getCalendar(id: string): Observable<Calendar> {
        const url = this.userService.getApiPrefix() + `${AdminCalendarService.CALENDAR_URL}/${id}`;
        return this.http.get(url)
            .map((response => response.json() as Calendar))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * provede update intervalu na serveru
     * @param calendar interval platnosti
     * @returns {Observable<Calendar>} observable s odpovedi ze serveru
     */
    update(calendar: Calendar): Observable<Calendar> {
        const url = this.userService.getApiPrefix() + `${AdminCalendarService.CALENDAR_URL}/${calendar.id}`;
        return this.http.put(url, JSON.stringify(calendar))
            .map((response => response.json() as Calendar))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vytvori interval na serveru
     * @param calendar interval platnosti
     * @returns {Observable<Calendar>} observable s odpovedi ze serveru
     */
    create(calendar: Calendar): Observable<Calendar> {
        const url = this.userService.getApiPrefix() + AdminCalendarService.CALENDAR_URL;
        return this.http.post(url, JSON.stringify(calendar))
            .map((response => response.json() as Calendar))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * smaze interval plastnosti na serveru
     * @param id id intervalu platnosti
     * @returns {Observable<void>} observable s odpovedi ze serveru
     */
    delete(id: string): Observable<void> {
        const url = this.userService.getApiPrefix() + `${AdminCalendarService.CALENDAR_URL}/${id}`;
        return this.http.delete(url)
            .map(() => null)
            .catch(err => this.errorService.handleServerError(err));
    }

}