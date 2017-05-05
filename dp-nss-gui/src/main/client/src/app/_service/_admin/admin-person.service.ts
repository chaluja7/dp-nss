import {Injectable} from "@angular/core";
import {ErrorService} from "../error.service";
import {HttpClient} from "../http-client";
import {Observable} from "rxjs";
import {AppSettings} from "../../_common/app.settings";
import {Person} from "../../_model/person";
import {ResetPassword} from "../../_model/reset-password";
import {TimeTable} from "../../_model/time-table";

/**
 * Obsluha administrace osob
 */
@Injectable()
export class AdminPersonService {

    private static PERSON_URL = 'admin/person';

    constructor(private http: HttpClient, private errorService: ErrorService) {
    }

    /**
     * vrati osoby ze serveru
     * @returns {Observable<Person[]>} observable s odpovedi ze serveru
     */
    getPersons(): Observable<Person[]> {
        const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}`;
        return this.http.get(url)
            .map((response => response.json() as TimeTable[]))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vrati osobu ze serveru
     * @param id id osoby
     * @returns {Observable<Person>} observable s odpovedi ze serveru
     */
    getPerson(id: number): Observable<Person> {
        const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}/${id}`;
        return this.http.get(url)
            .map((response => response.json() as Person))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * provede update osoby na serveru
     * @param person osoba
     * @returns {Observable<Person>} observable s odpovedi ze serveru
     */
    update(person: Person): Observable<Person> {
        const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}/${person.id}`;
        return this.http.put(url, JSON.stringify(person))
            .map((response => response.json() as Person))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * vytvori osobu na serveru
     * @param person osoba
     * @returns {Observable<Person>} observable s odpovedi ze serveru
     */
    create(person: Person): Observable<Person> {
        const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}`;
        return this.http.post(url, JSON.stringify(person))
            .map((response => response.json() as Person))
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * zmeni heslo osobe na serveru
     * @param id id osoby
     * @param resetPassword objekt zmeny hesla
     * @returns {Observable<void>} observable s odpovedi ze serveru
     */
    changePassword(id: number, resetPassword: ResetPassword): Observable<void> {
        const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}/${id}/resetPassword`;
        return this.http.put(url, JSON.stringify(resetPassword))
            .map(() => {
            })
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * smaze osobu na serveru
     * @param id id osoby
     * @returns {Observable<void>} observable s odpovedi ze serveru
     */
    delete(id: number): Observable<void> {
        const url = AppSettings.API_ENDPOINT + `${AdminPersonService.PERSON_URL}/${id}`;
        return this.http.delete(url)
            .map(() => null)
            .catch(err => this.errorService.handleServerError(err));
    }

}