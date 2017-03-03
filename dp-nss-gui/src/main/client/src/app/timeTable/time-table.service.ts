import {Injectable} from "@angular/core";
import {TimeTable} from "./time-table";
import {Http, Headers} from "@angular/http";
import "rxjs/add/operator/toPromise";

@Injectable()
export class TimeTableService {

  private timeTableUrl = '/api/v1/timeTable';

  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http) { }

  getTimeTables(): Promise<TimeTable[]> {
      return this.http.get(this.timeTableUrl)
          .toPromise()
          .then(response => response.json() as TimeTable[])
          .catch(this.handleError);
  }

  getTimeTable(id: string): Promise<TimeTable> {
    const url = `${this.timeTableUrl}/${id}`;
    return this.http.get(url)
        .toPromise()
        .then(response => response.json() as TimeTable)
        .catch(this.handleError);
  }

  update(timeTable: TimeTable): Promise<TimeTable> {
    const url = `${this.timeTableUrl}/${timeTable.id}`;
    return this.http
        .put(url, JSON.stringify(timeTable), {headers: this.headers})
        .toPromise()
        .then(() => timeTable)
        .catch(this.handleError);
  }

  create(timeTable: TimeTable): Promise<TimeTable> {
    return this.http
        .post(this.timeTableUrl, JSON.stringify(timeTable), {headers: this.headers})
        .toPromise()
        .then(response => response.json() as TimeTable)
        .catch(this.handleError);
  }

  delete(id: string): Promise<void> {
    const url = `${this.timeTableUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers})
        .toPromise()
        .then(() => null)
        .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred: ', error.status + ' - ' + error.json().message); // for demo purposes only
    return Promise.reject(error.json().message || error);
  }

}