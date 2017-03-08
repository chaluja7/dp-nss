import {Injectable} from "@angular/core";
import {TimeTable} from "../_model/time-table";
import "rxjs/add/operator/toPromise";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";

@Injectable()
export class TimeTableService {

  private timeTableUrl = '/api/v1/timeTable';

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  getTimeTables(): Promise<TimeTable[]> {
      return this.http.get(this.timeTableUrl)
          .toPromise()
          .then(response => response.json() as TimeTable[])
          .catch(this.errorService.handleError);
  }

  getTimeTable(id: string): Promise<TimeTable> {
    const url = `${this.timeTableUrl}/${id}`;
    return this.http.get(url)
        .toPromise()
        .then(response => response.json() as TimeTable)
        .catch(this.errorService.handleError);
  }

  update(timeTable: TimeTable): Promise<TimeTable> {
    const url = `${this.timeTableUrl}/${timeTable.id}`;
    return this.http
        .put(url, JSON.stringify(timeTable))
        .toPromise()
        .then(() => timeTable)
        .catch(this.errorService.handleError);
  }

  create(timeTable: TimeTable): Promise<TimeTable> {
    return this.http
        .post(this.timeTableUrl, JSON.stringify(timeTable))
        .toPromise()
        .then(response => response.json() as TimeTable)
        .catch(this.errorService.handleError);
  }

  delete(id: string): Promise<void> {
    const url = `${this.timeTableUrl}/${id}`;
    return this.http.delete(url)
        .toPromise()
        .then(() => null)
        .catch(this.errorService.handleError);
  }

}