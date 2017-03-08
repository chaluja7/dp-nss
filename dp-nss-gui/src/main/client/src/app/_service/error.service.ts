import {Injectable} from "@angular/core";
import {Response} from "@angular/http";
import {Observable} from "rxjs";

@Injectable()
export class ErrorService {

  public handleError(error: any): Promise<any> {
    console.error('An error occurred: ', error.status + ' - ' + error.json().message); // for demo purposes only
    return Promise.reject(error.json().message || error);
  }

  public handleServerError(error: Response): Observable<any> {
    return Observable.throw(error.json().error || 'Server error'); // Observable.throw() is undefined at runtime using Webpack
  }

}