import {Injectable} from "@angular/core";
import {LoggedUser} from "../_model/logged-user";

@Injectable()
export class UserService {

  public static LOGGED_USER_IDENT = 'currentUser';

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
  }

  removeUser(): void {
      localStorage.removeItem(UserService.LOGGED_USER_IDENT);
  }

}