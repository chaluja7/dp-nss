import {Injectable} from "@angular/core";
import {Response} from "@angular/http";
import {Observable} from "rxjs";
import {Router} from "@angular/router";
import {Location} from "@angular/common";

/**
 * Obsluha HTTP chyb
 */
@Injectable()
export class ErrorService {

    constructor(private router: Router, private location: Location) {
    }

    /**
     * handling serverove chyby (chybovy kod)
     * @param error chybova response ze serveru
     * @returns {any} observable s error message
     */
    public handleServerError(error: Response): Observable<any> {
        console.error('An error occurred: ', error.status + ' - ' + error.json().message);
        if (error.status === 401) {
            this.location.replaceState('/');
            //pokud je unauthorized tak globalne hazeme na prihlasovaci stranku a mazeme ulozene aut. hlavicky z localStorage
            this.router.navigate(['/login']);
            return Observable.empty();
        }

        return Observable.throw(error.json().message || 'Server error'); // Observable.throw() is undefined at runtime using Webpack
    }

}