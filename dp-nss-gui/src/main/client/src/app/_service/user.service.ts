import {Injectable} from "@angular/core";
import {LoggedUser} from "../_model/logged-user";
import {AppSettings} from "../_common/app.settings";
import {Cookie} from "ng2-cookies";

@Injectable()
export class UserService {

  public static LOGGED_USER_IDENT = 'currentUser';

  public static SELECTED_TIME_TABLE_IDENT = 'selectedTimeTable';

  public static SELECTED_TIME_TABLE_ACTIVE = 'selectedTimeTableActive';

  msg: string;

  importantMsg: boolean;

  isLoggedIn(): boolean {
      let x = Cookie.get(UserService.LOGGED_USER_IDENT);
      return x != null && x ? true : false;
  }

  isAdminLoggedIn(): boolean {
      let user = this.getLoggedUser();
      return user && user.isAdmin;
  }

  getLoggedUser(): LoggedUser {
      return this.isLoggedIn() ? JSON.parse(Cookie.get(UserService.LOGGED_USER_IDENT)) : null;
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

      Cookie.set(UserService.LOGGED_USER_IDENT, JSON.stringify(user));
      Cookie.delete(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  removeUser(): void {
      Cookie.delete(UserService.LOGGED_USER_IDENT);
      Cookie.delete(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  getSelectedTimeTable(): string {
      return Cookie.get(UserService.SELECTED_TIME_TABLE_IDENT);
  }

  isSelectedTimeTableActive(): boolean {
      let active = Cookie.get(UserService.SELECTED_TIME_TABLE_ACTIVE);
      return active == 'true';
  }

  storeSelectedTimeTable(timeTable: string, active: boolean): void {
      Cookie.set(UserService.SELECTED_TIME_TABLE_IDENT, timeTable);
      Cookie.set(UserService.SELECTED_TIME_TABLE_ACTIVE, active ? 'true' : 'false');
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