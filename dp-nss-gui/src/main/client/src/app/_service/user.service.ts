import {Injectable} from "@angular/core";
import {LoggedUser} from "../_model/logged-user";
import {AppSettings} from "../_common/app.settings";

@Injectable()
export class UserService {

  public static LOGGED_USER_IDENT = 'currentUser';

  public static SELECTED_TIME_TABLE_IDENT = 'selectedTimeTable';

  public static SELECTED_TIME_TABLE_ACTIVE = 'selectedTimeTableActive';

  msg: string;

  importantMsg: boolean;

  isLoggedIn(): boolean {
      return sessionStorage.getItem(UserService.LOGGED_USER_IDENT) != null;
  }

  isAdminLoggedIn(): boolean {
      let user = this.getLoggedUser();
      return user && user.isAdmin;
  }

  getLoggedUser(): LoggedUser {
      return this.isLoggedIn() ? JSON.parse(sessionStorage.getItem(UserService.LOGGED_USER_IDENT)) : null;
  }

  storeUser(user: LoggedUser): void {
      if(user && user.roles) {
          for(let role of user.roles) {
              if(role === 'ADMIN') {
                  user.isAdmin = true;
                  break;
              }
          }
      }

      sessionStorage.setItem(UserService.LOGGED_USER_IDENT, JSON.stringify(user));
      sessionStorage.removeItem(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  removeUser(): void {
      sessionStorage.removeItem(UserService.LOGGED_USER_IDENT);
      sessionStorage.removeItem(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  getSelectedTimeTable(): string {
      return sessionStorage.getItem(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  isSelectedTimeTableActive(): boolean {
      let active = sessionStorage.getItem(UserService.SELECTED_TIME_TABLE_ACTIVE);
      return active == 'true';
  }

  storeSelectedTimeTable(timeTable: string, active: boolean): void {
      sessionStorage.setItem(UserService.SELECTED_TIME_TABLE_IDENT, timeTable);
      sessionStorage.setItem(UserService.SELECTED_TIME_TABLE_ACTIVE, active ? 'true' : 'false');
  }

  getApiPrefix(): string {
      let currTimeTable = this.getSelectedTimeTable();
      if(currTimeTable) {
          return AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(currTimeTable) + "/";
      }

      return AppSettings.API_ENDPOINT;
  }

  setMsg(msg: string, important?: boolean, longDuration?: boolean) {
      if(this.importantMsg) {
          //pokud je aktualni important tak ji nechci prebit
          this.importantMsg = false;
          return;
      }

      this.msg = msg;
      if(important) this.importantMsg = true;

      //zobrazujeme jen na 3s
      setTimeout(() => {
          this.msg = null;
      }, longDuration ? 5000 : 2500);
  }

}