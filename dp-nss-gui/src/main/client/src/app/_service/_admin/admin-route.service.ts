import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Route} from "../../_model/search-result-model";

@Injectable()
export class AdminRouteService {

  static ROUTE_URL = 'admin/route';

  constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) { }

  getRoutes(filter: Route, offset: number, limit: number, order: string): Observable<Response> {
    let headers = new Headers();
    headers.append(AppSettings.OFFSET_HEADER, offset + '');
    headers.append(AppSettings.LIMIT_HEADER, limit + '');
    headers.append(AppSettings.ORDER_HEADER, order);

    let params: URLSearchParams = new URLSearchParams();
    if(filter.id) params.set('id', filter.id);
    if(filter.shortName) params.set('shortName', filter.shortName);
    if(filter.longName) params.set('longName', filter.longName);
    if(!isNaN(filter.typeCode) && filter.typeCode !== null) params.set('typeCode', filter.typeCode + '');
    if(filter.color) params.set('color', filter.color);
    if(filter.agencyId && filter.agencyId !== 'null') params.set('agencyId', filter.agencyId);

    return this.http
        .get(this.userService.getApiPrefix() + AdminRouteService.ROUTE_URL, params, headers)
        .map((response => response))
        .catch(err => this.errorService.handleServerError(err));
  }

  getRoute(id: string): Observable<Route> {
    const url = this.userService.getApiPrefix() + `${AdminRouteService.ROUTE_URL}/${id}`;
    return this.http.get(url)
        .map((response => response.json() as Route))
        .catch(err => this.errorService.handleServerError(err));
  }

  update(route: Route): Observable<Route> {
    const url = this.userService.getApiPrefix() + `${AdminRouteService.ROUTE_URL}/${route.id}`;
    return this.http.put(url, JSON.stringify(route))
        .map((response => response.json() as Route))
        .catch(err => this.errorService.handleServerError(err));
  }

  create(route: Route): Observable<Route> {
    const url = this.userService.getApiPrefix() + AdminRouteService.ROUTE_URL;
    return this.http.post(url, JSON.stringify(route))
        .map((response => response.json() as Route))
        .catch(err => this.errorService.handleServerError(err));
  }

  delete(id: string): Observable<void> {
    const url = this.userService.getApiPrefix() + `${AdminRouteService.ROUTE_URL}/${id}`;
    return this.http.delete(url)
        .map(() => null)
        .catch(err => this.errorService.handleServerError(err));
  }

}