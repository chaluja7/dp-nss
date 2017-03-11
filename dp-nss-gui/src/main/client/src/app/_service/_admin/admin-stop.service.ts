import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Stop} from "../../_model/search-result-model";

@Injectable()
export class AdminStopService {

  private static STOP_URL = 'admin/stop';

  constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) { }

  getStops(filter: Stop, offset: number, limit: number, order: string): Observable<Response> {
    let headers = new Headers();
    headers.append(AppSettings.OFFSET_HEADER, offset + '');
    headers.append(AppSettings.LIMIT_HEADER, limit + '');
    headers.append(AppSettings.ORDER_HEADER, order);

    let params: URLSearchParams = new URLSearchParams();
    if(filter.id) params.set('id', filter.id);
    if(filter.name) params.set('name', filter.name);
    if(!isNaN(filter.lat) && filter.lat !== null) params.set('lat', filter.lat + '');
    if(!isNaN(filter.lon) && filter.lat !== null) params.set('lon', filter.lon + '');
    if(filter.parentStopId) params.set('parentStopId', filter.parentStopId);

    return this.http
        .get(this.userService.getApiPrefix() + AdminStopService.STOP_URL, params, headers)
        .map((response => response))
        .catch(err => this.errorService.handleServerError(err));
  }

}