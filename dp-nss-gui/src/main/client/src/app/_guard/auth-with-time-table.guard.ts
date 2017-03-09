import {Injectable} from "@angular/core";
import {Router, CanActivate} from "@angular/router";
import {UserService} from "../_service/user.service";

@Injectable()
export class AuthWithTimeTableGuard implements CanActivate {

    constructor(private router: Router, private userService: UserService) { }

    canActivate() {
        if(this.userService.isLoggedIn() && this.userService.getSelectedTimeTable() !== null) {
            return true;
        }

        // not logged in so redirect to login page
        this.router.navigate(['/login']);
        return false;
    }
}