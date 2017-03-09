import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {HttpClient} from "../http-client";
import {Stop} from "../../_model/search-result-model";
import {ErrorService} from "../error.service";

@Injectable()
export class AdminStopService {

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  getStops(): Observable<Stop[]> {
    return this.http
        .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam('pid') + "/admin/stop")
        .map((response => response.json() as Stop[]))
        .catch(err => this.errorService.handleServerError(err));
  }

}