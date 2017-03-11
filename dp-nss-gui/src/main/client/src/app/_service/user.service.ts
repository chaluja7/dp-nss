import {Injectable} from "@angular/core";
import {LoggedUser} from "../_model/logged-user";
import {AppSettings} from "../_common/app.settings";

@Injectable()
export class UserService {

  public static LOGGED_USER_IDENT = 'currentUser';

  public static SELECTED_TIME_TABLE_IDENT = 'selectedTimeTable';

  msg: string;

  isLoggedIn(): boolean {
      return localStorage.getItem(UserService.LOGGED_USER_IDENT) != null;
  }

  isAdminLoggedIn(): boolean {
      let user = this.getLoggedUser();
      if(user && user.roles && user.roles.length > 0) {
          for(let role of user.roles) {
              if (role === 'ADMIN') {
                  return true;
              }
          }
      }

      return false;
  }

  getLoggedUser(): LoggedUser {
      return this.isLoggedIn() ? JSON.parse(localStorage.getItem(UserService.LOGGED_USER_IDENT)) : null;
  }

  storeUser(user: LoggedUser): void {
      localStorage.setItem(UserService.LOGGED_USER_IDENT, JSON.stringify(user));
      localStorage.removeItem(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  removeUser(): void {
      localStorage.removeItem(UserService.LOGGED_USER_IDENT);
      localStorage.removeItem(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  getSelectedTimeTable(): string {
      return localStorage.getItem(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  storeSelectedTimeTable(timeTable: string): void {
      localStorage.setItem(UserService.SELECTED_TIME_TABLE_IDENT, timeTable);
  }

  getApiPrefix(): string {
      let currTimeTable = this.getSelectedTimeTable();
      if(currTimeTable) {
          return AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(currTimeTable) + "/";
      }

      return AppSettings.API_ENDPOINT;
  }

  setMsg(msg: string) {
      this.msg = msg;

      //zobrazujeme jen na 3s
      setTimeout(() => {
          this.msg = null;
      }, 2500);
  }

}