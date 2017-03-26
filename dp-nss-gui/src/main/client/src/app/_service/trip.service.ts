import {Injectable} from "@angular/core";
import {AppSettings} from "../_common/app.settings";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {Observable} from "rxjs";

@Injectable()
export class TripService {

    constructor(private http: HttpClient, private errorService: ErrorService) { }

    getTrip(schema: string, id: string): Observable<any> {
        return this.http
            .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(schema) + "/trip/" + id)
            .map((response => response.json()))
            .catch(err => this.errorService.handleServerError(err));
    }

}