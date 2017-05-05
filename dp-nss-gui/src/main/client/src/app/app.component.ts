import {Component} from "@angular/core";
import {UserService} from "./_service/user.service";
import {HttpClient} from "./_service/http-client";

/**
 * Aplikacni komponenta, do ni vkladame obsah jednotlivych stranek.
 */
@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {

    constructor(private userService: UserService, public http: HttpClient) {
    }

    /**
     * @returns {boolean} true, if user is logged in
     */
    isLoggedIn(): boolean {
        return this.userService.isLoggedIn();
    }

    /**
     * @returns {boolean} true, if admin is logged in
     */
    isAdminLoggedIn(): boolean {
        return this.userService.isAdminLoggedIn();
    }

    /**
     * @returns {boolean} true, if user has timetable selected
     */
    isTimeTableSelected(): boolean {
        return this.userService.getSelectedTimeTable() !== null;
    }

    /**
     * @returns {boolean} true, if timetable selected by user is active
     */
    isSelectedTimeTableActive(): boolean {
        return this.userService.isSelectedTimeTableActive();
    }

    /**
     * @returns {number} id of currently logged user
     */
    getLoggedUserId(): number {
        return this.userService.getLoggedUser().id;
    }

    /**
     * @returns {boolean} true, if users password does not need to be changed
     */
    hasPassword(): boolean {
        return !this.userService.getLoggedUser().passwordChangeRequired;
    }

}
