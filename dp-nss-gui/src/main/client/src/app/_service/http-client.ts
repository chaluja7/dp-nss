import {Injectable} from "@angular/core";
import {Http, Headers, URLSearchParams} from "@angular/http";
import {AppSettings} from "../_common/app.settings";
import {UserService} from "./user.service";

@Injectable()
export class HttpClient {

  constructor(private http: Http, private userService: UserService) {}

  createAuthorizationHeader(headers: Headers) {
    headers.append('Content-Type', 'application/json');

    //pokud je uzivatel prihlaseny, tak pridame jeho auth hlavicku do vsech requestu
    let currentUser = this.userService.getLoggedUser();
    if(currentUser && currentUser.token) {
      headers.append(AppSettings.AUTH_HEADER, currentUser.token);
    }
  }

  get(url: string, params?: URLSearchParams) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    if(params) {
      return this.http.get(url, {
        headers: headers, search: params
      });
    } else {
      return this.http.get(url, {
        headers: headers
      });
    }
  }

  post(url: string, data) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    return this.http.post(url, data, {
      headers: headers
    });
  }

  put(url: string, data) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    return this.http.put(url, data, {
      headers: headers
    });
  }

  delete(url: string) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    return this.http.delete(url, {headers: headers});
  }

}