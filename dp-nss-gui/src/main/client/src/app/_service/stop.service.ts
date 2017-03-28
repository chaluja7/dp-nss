import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AppSettings} from "../_common/app.settings";
import {HttpClient} from "./http-client";
import {ErrorService} from "./error.service";
import {Stop} from "../_model/search-result-model";

@Injectable()
export class StopService {

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  getStop(schema: string, id: string): Observable<Stop> {
    return this.http.get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(schema) + '/stop/' + id)
        .map((response => response.json() as Stop))
        .catch(err => this.errorService.handleServerError(err));
  }

}