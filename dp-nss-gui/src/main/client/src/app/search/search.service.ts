import {Injectable} from "@angular/core";
import {Http, URLSearchParams} from "@angular/http";
import {AppSettings} from "../common/app.settings";
import {DateService} from "../common/date.service";
import {SearchResultModel} from "./search-result-model";
import {ErrorService} from "../common/error.service";

@Injectable()
export class SearchService {

    constructor(private http: Http, private errorService: ErrorService) { }

    search(timeTableId: string, stopFrom: string, stopTo: string, departure: Date, maxTransfers: number): Promise<SearchResultModel[]> {
        let params: URLSearchParams = new URLSearchParams();
        params.set('stopFromName', stopFrom);
        params.set('stopToName', stopTo);
        params.set('departure', DateService.getFormattedDate(departure));
        params.set('maxTransfers', maxTransfers + '');

        return this.http
            .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(timeTableId) + "/search", {search: params})
            .toPromise()
            .then(response => response.json() as SearchResultModel[])
            .catch(this.errorService.handleError);
    }


}