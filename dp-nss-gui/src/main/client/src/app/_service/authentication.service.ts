import {Injectable} from "@angular/core";
import {Response} from "@angular/http";
import {Observable} from "rxjs";
import "rxjs/add/operator/map";
import {AppSettings} from "../_common/app.settings";
import {User} from "../_model/user";
import {ErrorService} from "./error.service";
import {HttpClient} from "./http-client";
import {UserService} from "./user.service";

@Injectable()
export class AuthenticationService {

  constructor(private http: HttpClient, private userService: UserService, private errorService: ErrorService) {}

  login(username: string, password: string): Observable<boolean> {
    let user = new User();
    user.username = username;
    user.password = password;

    return this.http.post(AppSettings.API_ENDPOINT + 'login', JSON.stringify(user))
        .map((response: Response) => {
          let loggedUser = response.json();
          this.userService.storeUser(loggedUser);
        })
        .catch(err => this.errorService.handleServerError(err));
  }

  logout(): void {
    let user = this.userService.getLoggedUser();
    if(user) {
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
