import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Agency} from "../../_model/agency";

/**
 * Obsluha administrace dopravcu
 */
@Injectable()
export class AdminAgencyService {

    static AGENCY_URL = 'admin/agency';

    constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) {
    }

    /**
     * vrati dopravce ze serveru
     * @param filter filter
     * @param offset odsazeni
     * @param limit limit
     * @param order poradi
     * @returns {Observable<Response>} observable s odpovedi ze serveru
     */
    getAgencies(filter: Agency, offset: number, limit: number, order: string): Observable<Response> {
        let headers = new Headers();
        headers.append(AppSettings.OFFSET_HEADER, offset + '');
        headers.append(AppSettings.LIMIT_HEADER, limit + '');
        headers.append(AppSettings.ORDER_HEADER, order);

        let params: URLSearchParams = new URLSearchParams();
        if (filter.id) params.set('id', filter.id);
        if (filter.name) params.set('name', filter.name);
        if (filter.url) params.set('url', filter.url);
        if (filter.phone) params.set('phone', filter.phone);

        return this.http
            .get(this.userService.getApiPrefix() + AdminAgencyService.AGENCY_URL, params, headers)
            .map((response => response))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati dopravce pro ciselnik
     * @returns {Observable<Agency[]>} observable s odpovedi ze serveru
     */
    getAgenciesForSelectBox(): Observable<Agency[]> {
        let params: URLSearchParams = new URLSearchParams();
        params.set('allOptions', 'true');

        return this.http
            .get(this.userService.getApiPrefix() + AdminAgencyService.AGENCY_URL, params)
            .map((response => response.json() as Agency[]))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati dopravce ze serveru
     * @param id id dopravce
     * @returns {Observable<Agency>} observable s odpovedi ze serveru
     */
    getAgency(id: string): Observable<Agency> {
        const url = this.userService.getApiPrefix() + `${AdminAgencyService.AGENCY_URL}/${id}`;
        return this.http.get(url)
            .map((response => response.json() as Agency))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * provede update dopravce na serveru
     * @param agency dopravce
     * @returns {Observable<Agency>} observable s odpovedi ze serveru
     */
    update(agency: Agency): Observable<Agency> {
        const url = this.userService.getApiPrefix() + `${AdminAgencyService.AGENCY_URL}/${agency.id}`;
        return this.http.put(url, JSON.stringify(agency))
            .map((response => response.json() as Agency))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vytvori dopravce na serveru
     * @param agency dopravce
     * @returns {Observable<Agency>} observable s odpovedi ze serveru
     */
    create(agency: Agency): Observable<Agency> {
        const url = this.userService.getApiPrefix() + AdminAgencyService.AGENCY_URL;
        return this.http.post(url, JSON.stringify(agency))
            .map((response => response.json() as Agency))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * smaze dopravce na serveru
     * @param id id dopravce
     * @returns {Observable<void>} observable s odpovedi ze serveru
     */
    delete(id: string): Observable<void> {
        const url = this.userService.getApiPrefix() + `${AdminAgencyService.AGENCY_URL}/${id}`;
        return this.http.delete(url)
            .map(() => null)
            .catch(err => this.errorService.handleServerError(err));
    }

}