import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";
import {AppSettings} from "../common/app.settings";

@Injectable()
export class StopService {

  constructor(private http: Http) { }

  search(term: string, timeTableId: string): Observable<string[]> {
    return this.http
        .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(timeTableId) + "?startsWith=" + term)
        .map(response => response.json() as string[]);
  }

}