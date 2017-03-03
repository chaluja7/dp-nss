import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";

@Injectable()
export class StopService {

  private timeTableUrl = '/api/v1/x-pid/stop';

  constructor(private http: Http) { }

  search(term: string): Observable<string[]> {
    return this.http
        .get(this.timeTableUrl + "?startsWith=" + term)
        .map(response => response.json() as string[]);
  }

}