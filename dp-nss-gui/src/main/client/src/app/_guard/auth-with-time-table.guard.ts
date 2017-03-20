import {Injectable} from "@angular/core";
import {Router, CanActivate} from "@angular/router";
import {UserService} from "../_service/user.service";

@Injectable()
export class AuthWithTimeTableGuard implements CanActivate {

    constructor(private router: Router, private userService: UserService) { }

    canActivate() {
        if(this.userService.isLoggedIn() && this.userService.getSelectedTimeTable() !== null && this.userService.isSelectedTimeTableActive()) {
            return true;
        }

        // not logged in or inactive timetable so redirect to login page for safety reasons
        this.router.navigate(['/login']);
        return false;
    }
}