import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Stop} from "../../_model/search-result-model";

/**
 * Obsluha administrace stanic
 */
@Injectable()
export class AdminStopService {

    static STOP_URL = 'admin/stop';

    constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) {
    }

    /**
     * vrati stanice ze serveru
     * @param filter filter
     * @param offset odsazeni
     * @param limit limit
     * @param order poradi
     * @returns {Observable<Response>} observable s odpovedi ze serveru
     */
    getStops(filter: Stop, offset: number, limit: number, order: string): Observable<Response> {
        let headers = new Headers();
        headers.append(AppSettings.OFFSET_HEADER, offset + '');
        headers.append(AppSettings.LIMIT_HEADER, limit + '');
        headers.append(AppSettings.ORDER_HEADER, order);

        let params: URLSearchParams = new URLSearchParams();
        if (filter.id) params.set('id', filter.id);
        if (filter.name) params.set('name', filter.name);
        if (!isNaN(filter.lat) && filter.lat !== null) params.set('lat', filter.lat + '');
        if (!isNaN(filter.lon) && filter.lat !== null) params.set('lon', filter.lon + '');
        if (!isNaN(filter.wheelChairCode) && filter.wheelChairCode !== null) params.set('wheelChairCode', filter.wheelChairCode + '');
        if (filter.parentStopId) params.set('parentStopId', filter.parentStopId);

        return this.http
            .get(this.userService.getApiPrefix() + AdminStopService.STOP_URL, params, headers)
            .map((response => response))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati stanici ze serveru
     * @param id id stanice
     * @returns {Observable<Stop>} observable s odpovedi ze serveru
     */
    getStop(id: string): Observable<Stop> {
        const url = this.userService.getApiPrefix() + `${AdminStopService.STOP_URL}/${id}`;
        return this.http.get(url)
            .map((response => response.json() as Stop))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * provede update stanice na serveru
     * @param stop stanice
     * @returns {Observable<Stop>} observable s odpovedi ze serveru
     */
    update(stop: Stop): Observable<Stop> {
        const url = this.userService.getApiPrefix() + `${AdminStopService.STOP_URL}/${stop.id}`;
        return this.http.put(url, JSON.stringify(stop))
            .map((response => response.json() as Stop))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vytvori stanici na serveru
     * @param stop stanice
     * @returns {Observable<Stop>} observable s odpovedi ze serveru
     */
    create(stop: Stop): Observable<Stop> {
        const url = this.userService.getApiPrefix() + AdminStopService.STOP_URL;
        return this.http.post(url, JSON.stringify(stop))
            .map((response => response.json() as Stop))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * smaze stanici na serveru
     * @param id id stanice
     * @returns {Observable<void>} observable s odpovedi ze serveru
     */
    delete(id: string): Observable<void> {
        const url = this.userService.getApiPrefix() + `${AdminStopService.STOP_URL}/${id}`;
        return this.http.delete(url)
            .map(() => null)
            .catch(err => this.errorService.handleServerError(err));
    }

}