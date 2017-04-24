import {Component} from "@angular/core";
import {UserService} from "./_service/user.service";
import {HttpClient} from "./_service/http-client";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private userService: UserService, public http: HttpClient) {}

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  isAdminLoggedIn(): boolean {
    return this.userService.isAdminLoggedIn();
  }

  isTimeTableSelected(): boolean {
    return this.userService.getSelectedTimeTable() !== null;
  }

  isSelectedTimeTableActive(): boolean {
    return this.userService.isSelectedTimeTableActive();
  }

  getLoggedUserId(): number {
    return this.userService.getLoggedUser().id;
  }

  hasPassword(): boolean {
    return !this.userService.getLoggedUser().passwordChangeRequired;
  }

}
