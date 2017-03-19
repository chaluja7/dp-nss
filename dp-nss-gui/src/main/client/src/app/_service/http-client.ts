import {Injectable} from "@angular/core";
import {Http, Headers, URLSearchParams, Response, RequestOptions, ResponseContentType} from "@angular/http";
import {AppSettings} from "../_common/app.settings";
import {UserService} from "./user.service";
import {Observable} from "rxjs";
import * as FileSaver from "file-saver";

@Injectable()
export class HttpClient {

  constructor(private http: Http, private userService: UserService) {}

  numberOfPendingRequests: number = 0;

  createAuthorizationHeader(headers: Headers, emptyContentType?: boolean) {
    if(!emptyContentType) {
      headers.append('Content-Type', 'application/json');
    }

    //pokud je uzivatel prihlaseny, tak pridame jeho auth hlavicku do vsech requestu
    let currentUser = this.userService.getLoggedUser();
    if(currentUser && currentUser.token) {
      headers.append(AppSettings.AUTH_HEADER, currentUser.token);
    }
  }

  get(url: string, params?: URLSearchParams, headers?: Headers, downloadFile?: boolean): Observable<Response> {
    if(!headers) {
      headers = new Headers();
    }
    this.createAuthorizationHeader(headers);

    this.numberOfPendingRequests++;
    if(params) {
      return this.http.get(url, {
        headers: headers, search: params
      }).finally(() => {this.numberOfPendingRequests--});
    } else if(downloadFile) {
      let options = new RequestOptions({headers: headers, responseType: ResponseContentType.Blob});
      return this.http.get(url, options).finally(() => {this.numberOfPendingRequests--});
    } else {
      return this.http.get(url, {
        headers: headers
      }).finally(() => {this.numberOfPendingRequests--});
    }
  }

  post(url: string, data: any, emptyContentType?: boolean): Observable<Response> {
    let headers = new Headers();
    this.createAuthorizationHeader(headers, emptyContentType);

    this.numberOfPendingRequests++;
    return this.http.post(url, data, {
      headers: headers
    }).finally(() => {this.numberOfPendingRequests--});
  }

  put(url: string, data): Observable<Response> {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);

    this.numberOfPendingRequests++;
    return this.http.put(url, data, {
      headers: headers
    }).finally(() => {this.numberOfPendingRequests--});
  }

  delete(url: string): Observable<Response> {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);

    this.numberOfPendingRequests++;
    return this.http.delete(url, {headers: headers}).finally(() => {this.numberOfPendingRequests--});
  }

  extractZipContent(res: Response, fileName: string) {
    let blob: Blob = res.blob();
    FileSaver.saveAs(blob, fileName + '.zip');
  }

}