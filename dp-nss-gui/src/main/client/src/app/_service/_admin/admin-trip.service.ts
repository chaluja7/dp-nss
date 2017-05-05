import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Trip} from "../../_model/trip";

/**
 * Obsluha administrace jizdy.
 */
@Injectable()
export class AdminTripService {

    static TRIP_URL = 'admin/trip';

    constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) {
    }

    /**
     * Vrati jizdy ze serveru
     * @param filter filter jizd
     * @param offset odsazeni
     * @param limit max pocet polozek
     * @param order poradi
     * @returns {Observable<Response>} observable s odpovedi ze serveru
     */
    getTrips(filter: Trip, offset: number, limit: number, order: string): Observable<Response> {
        let headers = new Headers();
        headers.append(AppSettings.OFFSET_HEADER, offset + '');
        headers.append(AppSettings.LIMIT_HEADER, limit + '');
        headers.append(AppSettings.ORDER_HEADER, order);

        let params: URLSearchParams = new URLSearchParams();
        if (filter.id) params.set('id', filter.id);
        if (filter.headSign) params.set('headSign', filter.headSign);
        if (filter.shapeId) params.set('shapeId', filter.shapeId);
        if (filter.calendarId) params.set('calendarId', filter.calendarId);
        if (filter.routeShortName) params.set('routeShortName', filter.routeShortName);
        if (!isNaN(filter.wheelChairCode) && filter.wheelChairCode !== null) params.set('wheelChairCode', filter.wheelChairCode + '');

        return this.http
            .get(this.userService.getApiPrefix() + AdminTripService.TRIP_URL, params, headers)
            .map((response => response))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati jizdu ze serveru
     * @param id id jizdy
     * @returns {Observable<Trip>} observable s odpovedi ze serveru
     */
    getTrip(id: string): Observable<Trip> {
        const url = this.userService.getApiPrefix() + `${AdminTripService.TRIP_URL}/${id}`;
        return this.http.get(url)
            .map((response => response.json() as Trip))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * provede update jizdy na serveru
     * @param trip jizda
     * @returns {Observable<Trip>} observable s odpovedi ze serveru
     */
    update(trip: Trip): Observable<Trip> {
        const url = this.userService.getApiPrefix() + `${AdminTripService.TRIP_URL}/${trip.id}`;
        return this.http.put(url, JSON.stringify(trip))
            .map((response => response.json() as Trip))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vytvori jizdu na serveru
     * @param trip jizda
     * @returns {Observable<Trip>} observable s odpovedi ze serveru
     */
    create(trip: Trip): Observable<Trip> {
        const url = this.userService.getApiPrefix() + AdminTripService.TRIP_URL;
        return this.http.post(url, JSON.stringify(trip))
            .map((response => response.json() as Trip))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * smaze jizdu na serveru
     * @param id id jizdy
     * @returns {Observable<void>} observable s odpovedi ze serveru
     */
    delete(id: string): Observable<void> {
        const url = this.userService.getApiPrefix() + `${AdminTripService.TRIP_URL}/${id}`;
        return this.http.delete(url)
            .map(() => null)
            .catch(err => this.errorService.handleServerError(err));
    }

}