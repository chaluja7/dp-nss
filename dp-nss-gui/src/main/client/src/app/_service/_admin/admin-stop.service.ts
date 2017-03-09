import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {ErrorService} from "../error.service";
import {UserService} from "../user.service";
import {Headers, Response} from "@angular/http";

@Injectable()
export class AdminStopService {

  private static STOP_URL = 'admin/stop';

  constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) { }

  getStops(offset: number, limit: number, order: string): Observable<Response> {
    let headers = new Headers();
    headers.append(AppSettings.OFFSET_HEADER, offset + '');
    headers.append(AppSettings.LIMIT_HEADER, limit + '');
    headers.append(AppSettings.ORDER_HEADER, order);

    return this.http
        .get(this.userService.getApiPrefix() + AdminStopService.STOP_URL, null, headers)
        .map((response => response))
        .catch(err => this.errorService.handleServerError(err));
  }

}