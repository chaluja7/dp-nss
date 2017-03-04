import {Injectable} from "@angular/core";

@Injectable()
export class ErrorService {

  public handleError(error: any): Promise<any> {
    console.error('An error occurred: ', error.status + ' - ' + error.json().message); // for demo purposes only
    return Promise.reject(error.json().message || error);
  }

}