import {Injectable} from "@angular/core";
import {URLSearchParams} from "@angular/http";
import {AppSettings} from "../_common/app.settings";
import {DateService} from "./date.service";
import {SearchResultModel} from "../_model/search-result-model";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {Observable} from "rxjs";

@Injectable()
export class SearchService {

    constructor(private http: HttpClient, private errorService: ErrorService) { }

    search(timeTableId: string, stopFrom: string, stopTo: string, stopThrough: string, departureDate: Date, departureTime: Date, maxTransfers: number, withWheelChair: boolean): Observable<SearchResultModel[]> {
        let params: URLSearchParams = new URLSearchParams();
        params.set('stopFromName', stopFrom);
        params.set('stopToName', stopTo);
        if(stopThrough) params.set('stopThroughName', stopThrough);
        params.set('departure', DateService.getFormattedDate(departureDate, departureTime));
        params.set('maxTransfers', maxTransfers + '');
        params.set('withWheelChair', withWheelChair + '');

        return this.http
            .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(timeTableId) + "/search", params)
            .map((response => response.json() as SearchResultModel[]))
            .catch(err => this.errorService.handleServerError(err));
    }

}