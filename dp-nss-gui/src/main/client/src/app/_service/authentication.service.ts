import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import "rxjs/add/operator/map";
import {AppSettings} from "../_common/app.settings";
import {User} from "../_model/user";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {UserService} from "./user.service";
import {Person} from "../_model/person";

/**
 * sprava prihlaseni a odhlaseni
 */
@Injectable()
export class AuthenticationService {

    constructor(private http: HttpClient, private userService: UserService, private errorService: ErrorService) {
    }

    /**
     * pokusi se prihlasit uzivatele na server dle jmena a hesla
     * @param username uziv. jmeno
     * @param password heslo
     * @returns {Observable<Person>} observable s odpovedi z autentizacniho endpointu
     */
    login(username: string, password: string): Observable<Person> {
        let user = new User();
        user.username = username;
        user.password = password;

        return this.http.post(AppSettings.API_ENDPOINT + 'login', JSON.stringify(user))
            .map(response => {
                let loggedUser = response.json();
                this.userService.storeUser(loggedUser);
                return loggedUser as Person;
            })
            .catch(err => this.errorService.handleServerError(err));
    }

    /**
     * odhlasi uzivatele ze serveru
     */
    logout(): void {
        let user = this.userService.getLoggedUser();
        if (user) {
            this.http.post(AppSettings.API_ENDPOINT + 'logout', {})
                .toPromise()
                .then(neco => {
                    this.userService.removeUser();
                    this.userService.setMsg(AppSettings.LOGOUT_SUCCESS);
                })
                .catch(err => this.userService.removeUser());
        }

    }

}
