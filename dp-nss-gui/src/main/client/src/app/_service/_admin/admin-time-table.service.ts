import {Injectable} from "@angular/core";
import {TimeTable} from "../../_model/time-table";
import "rxjs/add/operator/toPromise";
import {ErrorService} from "../error.service";
import {HttpClient} from "../http-client";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {Response} from "@angular/http";
import {UserService} from "../user.service";

/**
 * obsluha administrace jizdnich radu
 */
@Injectable()
export class AdminTimeTableService {

    private static TIME_TABLE_URL = 'admin/timeTable';

    constructor(private http: HttpClient, private errorService: ErrorService, private userService: UserService) {
    }

    /**
     * vrati jizdni rady ze serveru
     * @returns {Observable<TimeTable[]>} observable s odpovedi ze serveru
     */
    getTimeTables(): Observable<TimeTable[]> {
        return this.http.get(AppSettings.API_ENDPOINT + AdminTimeTableService.TIME_TABLE_URL)
            .map((response => response.json() as TimeTable[]))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati jizdni rad ze serveru
     * @param id id jizdniho radu
     * @returns {Observable<TimeTable>} observable s odpovedi ze serveru
     */
    getTimeTable(id: string): Observable<TimeTable> {
        const url = AppSettings.API_ENDPOINT + `${AdminTimeTableService.TIME_TABLE_URL}/${id}`;
        return this.http.get(url)
            .map((response => response.json() as TimeTable))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * provede udpate jizdniho radu na serveru
     * @param timeTable jizdni rad
     * @returns {Observable<TimeTable>} observable s odpovedi ze serveru
     */
    update(timeTable: TimeTable): Observable<TimeTable> {
        const url = AppSettings.API_ENDPOINT + `${AdminTimeTableService.TIME_TABLE_URL}/${timeTable.id}`;
        return this.http.put(url, JSON.stringify(timeTable))
            .map((response => response.json() as TimeTable))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * stahne kompletni jizdni rad ze serveru
     * @returns {Observable<Response>} observable s odpovedi ze serveru
     */
    downloadFile(): Observable<Response> {
        return this.http.get(this.userService.getApiPrefix() + 'admin/gtfs/download', null, null, true)
            .map(response => response)
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * nahraje kompletni jizdni rad na server
     * @param fileName jmeno souboru
     * @param file soubor jizdnich radu
     * @returns {Observable<Response>} observable s odpovedi ze serveru
     */
    uploadFile(fileName: string, file: File): Observable<Response> {
        let formData: FormData = new FormData();
        formData.append(fileName, file, file.name);

        return this.http.post(this.userService.getApiPrefix() + 'admin/gtfs/upload', formData, true)
            .map(response => response)
            .catch(err => this.errorService.handleServerError(err));
    }

}