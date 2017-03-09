import {Injectable} from "@angular/core";
import {TimeTable} from "../_model/time-table";
import "rxjs/add/operator/toPromise";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {Observable} from "rxjs";
import {AppSettings} from "../_common/app.settings";

@Injectable()
export class TimeTableService {

  private timeTableUrl = 'timeTable';

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  getTimeTables(): Observable<TimeTable[]> {
      return this.http.get(AppSettings.API_ENDPOINT + this.timeTableUrl)
          .map((response => response.json() as TimeTable[]))
          .catch(err => this.errorService.handleServerError(err));
  }

}