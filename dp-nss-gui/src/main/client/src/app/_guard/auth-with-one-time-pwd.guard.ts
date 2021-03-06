import {Injectable} from "@angular/core";
import {Router, CanActivate} from "@angular/router";
import {UserService} from "../_service/user.service";

/**
 * Guard vyzadujici prihlaseneho uzivatele a povoli i uzivatele s jednorazovym heslem
 */
@Injectable()
export class AuthWithOneTimePwdGuard implements CanActivate {

    constructor(private router: Router, private userService: UserService) {
    }

    canActivate() {
        if (this.userService.isLoggedIn()) {
            return true;
        }

        // not logged in so redirect to login page
        this.router.navigate(['/login']);
        return false;
    }
}