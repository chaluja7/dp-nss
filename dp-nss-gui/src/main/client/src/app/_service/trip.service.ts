import {Injectable} from "@angular/core";
import {AppSettings} from "../_common/app.settings";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {Observable} from "rxjs";
import {URLSearchParams} from "@angular/http";

/**
 * Sprava jizd.
 */
@Injectable()
export class TripService {

    constructor(private http: HttpClient, private errorService: ErrorService) {
    }

    /**
     * @param schema id jizdniho radu
     * @param id id jizdy
     * @param withShapes true, pokud chci i prujezdni body
     * @returns {Observable<any>} observable s nalezenou jizdou
     */
    getTrip(schema: string, id: string, withShapes: boolean): Observable<any> {
        let params: URLSearchParams = new URLSearchParams();
        if (withShapes) {
            params.set('withShapes', 'true');
        }

        return this.http
            .get(AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(schema) + "/trip/" + id, params)
            .map((response => response.json()))
            .catch(err => this.errorService.handleServerError(err));
    }

}