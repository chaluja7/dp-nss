import {Injectable} from "@angular/core";
import {LoggedUser} from "../_model/logged-user";

@Injectable()
export class UserService {

  public static LOGGED_USER_IDENT = 'currentUser';

  public static SELECTED_TIME_TABLE_IDENT = 'selectedTimeTable';

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

}