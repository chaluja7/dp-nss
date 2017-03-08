import {Component} from "@angular/core";
import {UserService} from "./_service/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private userService: UserService) {}

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  isAdminLoggedIn(): boolean {
    return this.userService.isAdminLoggedIn();
  }

}
