import {Injectable} from "@angular/core";
import {TimeTable} from "../../_model/time-table";
import "rxjs/add/operator/toPromise";
import {ErrorService} from "../error.service";
import {HttpClient} from "../http-client";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {Response} from "@angular/http";
import {UserService} from "../user.service";

@Injectable()
export class AdminTimeTableService {

  private static TIME_TABLE_URL = 'admin/timeTable';

  constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) { }

  getTimeTables(): Observable<TimeTable[]> {
      return this.http.get(AppSettings.API_ENDPOINT + AdminTimeTableService.TIME_TABLE_URL)
          .map((response => response.json() as TimeTable[]))
          .catch(err => this.errorService.handleServerError(err));
  }

  getTimeTable(id: string): Observable<TimeTable> {
    const url = AppSettings.API_ENDPOINT + `${AdminTimeTableService.TIME_TABLE_URL}/${id}`;
    return this.http.get(url)
        .map((response => response.json() as TimeTable))
        .catch(err => this.errorService.handleServerError(err));
  }

  update(timeTable: TimeTable): Observable<TimeTable> {
    const url = AppSettings.API_ENDPOINT + `${AdminTimeTableService.TIME_TABLE_URL}/${timeTable.id}`;
    return this.http.put(url, JSON.stringify(timeTable))
        .map((response => response.json() as TimeTable))
        .catch(err => this.errorService.handleServerError(err));
  }

  downloadFile(): Observable<Response> {
    return this.http.get( this.userService.getApiPrefix() + 'admin/gtfs/download', null, null, true)
        .map(response => response)
        .catch(err => this.errorService.handleServerError(err));
  }

  // create(timeTable: TimeTable): Promise<TimeTable> {
  //   return this.http
  //       .post(this.TIME_TABLE_URL, JSON.stringify(timeTable))
  //       .toPromise()
  //       .then(response => response.json() as TimeTable)
  //       .catch(this.errorService.handleError);
  // }
  //
  // delete(id: string): Promise<void> {
  //   const url = `${this.TIME_TABLE_URL}/${id}`;
  //   return this.http.delete(url)
  //       .toPromise()
  //       .then(() => null)
  //       .catch(this.errorService.handleError);
  // }

}