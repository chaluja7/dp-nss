import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../_common/app.settings";
import {HttpClient} from "./http-client";
import {Stop} from "../_model/search-result-model";
import {ErrorService} from "./error.service";

@Injectable()
export class StopService {

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  search(term: string, timeTableId: string): Observable<string[]> {
    return this.http
        .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(timeTableId) + "?startsWith=" + term)
        .map(response => response.json() as string[]);
  }

  getStops(): Observable<Stop[]> {
    return this.http
        .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam('pid') + "/admin/stop")
        .map((response => response.json() as Stop[]))
        .catch(this.errorService.handleServerError);
  }

}