import {Injectable} from "@angular/core";
import {ErrorService} from "../error.service";
import {HttpClient} from "../http-client";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {Person} from "../../_model/person";
import {ResetPassword} from "../../_model/reset-password";
import {TimeTable} from "../../_model/time-table";

@Injectable()
export class AdminPersonService {

  private static PERSON_URL = 'admin/person';

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  getPersons(): Observable<Person[]> {
    const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}`;
    return this.http.get(url)
        .map((response => response.json() as TimeTable[]))
        .catch(err => this.errorService.handleServerError(err));
  }

  getPerson(id: number): Observable<Person> {
    const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}/${id}`;
    return this.http.get(url)
        .map((response => response.json() as Person))
        .catch(err => this.errorService.handleServerError(err));
  }

  changePassword(id: number, resetPassword: ResetPassword): Observable<void> {
      const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}/${id}/resetPassword`;
      return this.http.put(url, JSON.stringify(resetPassword))
          .map(() => {})
          .catch(err => this.errorService.handleServerError(err));
  }

}