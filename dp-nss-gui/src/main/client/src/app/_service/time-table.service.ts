import {Injectable} from "@angular/core";
import {TimeTable} from "../_model/time-table";
import "rxjs/add/operator/toPromise";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {Observable} from "rxjs";
import {AppSettings} from "../_common/app.settings";

/**
 * Sprava jizdnich radu
 */
@Injectable()
export class TimeTableService {

    private static TIME_TABLE_URL = 'timeTable';

    constructor(private http: HttpClient, private errorService: ErrorService) {
    }

    /**
     * @returns {Observable<TimeTable[]>} observable vsech aktivnich jizdni radu
     */
    getTimeTables(): Observable<TimeTable[]> {
        return this.http.get(AppSettings.API_ENDPOINT + TimeTableService.TIME_TABLE_URL)
            .map((response => response.json() as TimeTable[]))
            .catch(err => this.errorService.handleServerError(err));
    }

}