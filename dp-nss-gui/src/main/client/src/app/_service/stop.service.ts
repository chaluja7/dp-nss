import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../_common/app.settings";
import {HttpClient} from "./http-client";
import {ErrorService} from "./error.service";

@Injectable()
export class StopService {

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  search(term: string, timeTableId: string): Observable<string[]> {
    return this.http
        .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(timeTableId) + "?startsWith=" + term)
        .map(response => response.json() as string[])
        .catch(err => this.errorService.handleServerError(err));
  }

}