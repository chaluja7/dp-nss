import {Injectable} from "@angular/core";
import {URLSearchParams} from "@angular/http";
import {AppSettings} from "../_common/app.settings";
import {DateService} from "./date.service";
import {SearchResultModel} from "../_model/search-result-model";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {Observable} from "rxjs";
import {SearchModel} from "../_model/search-model";

/**
 * Sprava vyhledavani spoju
 */
@Injectable()
export class SearchService {

    constructor(private http: HttpClient, private errorService: ErrorService) {
    }

    /**
     * @param searchModel objekt vyhledavaciho pozadavku
     * @returns {Observable<SearchResultModel[]>} observable nalezenych vysledku vyhledavani
     */
    search(searchModel: SearchModel): Observable<SearchResultModel[]> {
        let params: URLSearchParams = new URLSearchParams();
        params.set('stopFromName', searchModel.stopFrom);
        params.set('stopToName', searchModel.stopTo);
        if (searchModel.stopThrough) params.set('stopThroughName', searchModel.stopThrough);
        params.set('date', DateService.getFormattedDate(searchModel.date, searchModel.time));
        params.set('maxTransfers', searchModel.maxNumOfTransfers + '');
        params.set('withWheelChair', (searchModel.wheelChair === true) + '');
        params.set('byArrival', (searchModel.byArrival === true) + '');

        return this.http
            .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(searchModel.timeTableId) + "/search", params)
            .map((response => response.json() as SearchResultModel[]))
            .catch(err => this.errorService.handleServerError(err));
    }

}