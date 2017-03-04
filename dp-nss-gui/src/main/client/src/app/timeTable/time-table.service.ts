import {Injectable} from "@angular/core";
import {TimeTable} from "./time-table";
import {Http, Headers} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {ErrorService} from "../common/error.service";

@Injectable()
export class TimeTableService {

  private timeTableUrl = '/api/v1/timeTable';

  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http, private errorService: ErrorService) { }

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
        .put(url, JSON.stringify(timeTable), {headers: this.headers})
        .toPromise()
        .then(() => timeTable)
        .catch(this.errorService.handleError);
  }

  create(timeTable: TimeTable): Promise<TimeTable> {
    return this.http
        .post(this.timeTableUrl, JSON.stringify(timeTable), {headers: this.headers})
        .toPromise()
        .then(response => response.json() as TimeTable)
        .catch(this.errorService.handleError);
  }

  delete(id: string): Promise<void> {
    const url = `${this.timeTableUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers})
        .toPromise()
        .then(() => null)
        .catch(this.errorService.handleError);
  }

}