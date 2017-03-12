import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response, URLSearchParams} from "@angular/http";
import {Agency} from "../../_model/agency";

@Injectable()
export class AdminAgencyService {

  static AGENCY_URL = 'admin/agency';

  constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) { }

  getAgencies(filter: Agency, offset: number, limit: number, order: string): Observable<Response> {
    let headers = new Headers();
    headers.append(AppSettings.OFFSET_HEADER, offset + '');
    headers.append(AppSettings.LIMIT_HEADER, limit + '');
    headers.append(AppSettings.ORDER_HEADER, order);

    let params: URLSearchParams = new URLSearchParams();
    if(filter.id) params.set('id', filter.id);
    if(filter.name) params.set('name', filter.name);
    if(filter.url) params.set('url', filter.url);
    if(filter.phone) params.set('phone', filter.phone);

    return this.http
        .get(this.userService.getApiPrefix() + AdminAgencyService.AGENCY_URL, params, headers)
        .map((response => response))
        .catch(err => this.errorService.handleServerError(err));
  }

  getAgenciesForSelectBox(): Observable<Agency[]> {
    let params: URLSearchParams = new URLSearchParams();
    params.set('allOptions', 'true');

    return this.http
        .get(this.userService.getApiPrefix() + AdminAgencyService.AGENCY_URL, params)
        .map((response => response.json() as Agency[]))
        .catch(err => this.errorService.handleServerError(err));
  }

  getAgency(id: string): Observable<Agency> {
    const url = this.userService.getApiPrefix() + `${AdminAgencyService.AGENCY_URL}/${id}`;
    return this.http.get(url)
        .map((response => response.json() as Agency))
        .catch(err => this.errorService.handleServerError(err));
  }

  update(agency: Agency): Observable<Agency> {
    const url = this.userService.getApiPrefix() + `${AdminAgencyService.AGENCY_URL}/${agency.id}`;
    return this.http.put(url, JSON.stringify(agency))
        .map((response => response.json() as Agency))
        .catch(err => this.errorService.handleServerError(err));
  }

  create(agency: Agency): Observable<Agency> {
    const url = this.userService.getApiPrefix() + AdminAgencyService.AGENCY_URL;
    return this.http.post(url, JSON.stringify(agency))
        .map((response => response.json() as Agency))
        .catch(err => this.errorService.handleServerError(err));
  }

  delete(id: string): Observable<void> {
    const url = this.userService.getApiPrefix() + `${AdminAgencyService.AGENCY_URL}/${id}`;
    return this.http.delete(url)
        .map(() => null)
        .catch(err => this.errorService.handleServerError(err));
  }

}