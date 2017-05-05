import {Injectable} from "@angular/core";
import {LoggedUser} from "../_model/logged-user";
import {AppSettings} from "../_common/app.settings";
import {Cookie} from "ng2-cookies";

/**
 * Sprava uzivatele a jizdnich radu
 */
@Injectable()
export class UserService {

    public static LOGGED_USER_IDENT = 'currentUser';

    public static SELECTED_TIME_TABLE_IDENT = 'selectedTimeTable';

    public static SELECTED_TIME_TABLE_ACTIVE = 'selectedTimeTableActive';

    msg: string;

    importantMsg: boolean;

    /**
     * @returns {boolean} true, pokud je aktualne uzivatel prihlaseny
     */
    isLoggedIn(): boolean {
        let x = Cookie.get(UserService.LOGGED_USER_IDENT);
        return x != null && x ? true : false;
    }

    /**
     * @returns {LoggedUser|boolean} true, pokud je aktualne uzivatel prihlaseny a je admin
     */
    isAdminLoggedIn(): boolean {
        let user = this.getLoggedUser();
        return user && user.isAdmin;
    }

    /**
     * @returns objekt prihlaseneho uzivatele nebo null
     */
    getLoggedUser(): LoggedUser {
        return this.isLoggedIn() ? JSON.parse(Cookie.get(UserService.LOGGED_USER_IDENT)) : null;
    }

    /**
     * @param user ulozi prihlaseneho uzivatele do cookie
     */
    storeUser(user: LoggedUser): void {
        if (user && user.roles) {
            for (let role of user.roles) {
                if (role === 'ADMIN') {
                    user.isAdmin = true;
                    break;
                }
            }
        }

        Cookie.set(UserService.LOGGED_USER_IDENT, JSON.stringify(user));
        Cookie.delete(UserService.SELECTED_TIME_TABLE_IDENT);
    }

    /**
     * odstrani prihlaseneho uzivatele z cookie
     */
    removeUser(): void {
        Cookie.delete(UserService.LOGGED_USER_IDENT);
        Cookie.delete(UserService.SELECTED_TIME_TABLE_IDENT);
    }

    /**
     * @returns {string} vrati aktualne vybrani jizdni rad prihlaseneho uzivatele
     */
    getSelectedTimeTable(): string {
        return Cookie.get(UserService.SELECTED_TIME_TABLE_IDENT);
    }

    /**
     * @returns {boolean} true, pokud je aktualne vybrany jizdni rad uzivatele aktivni
     */
    isSelectedTimeTableActive(): boolean {
        let active = Cookie.get(UserService.SELECTED_TIME_TABLE_ACTIVE);
        return active == 'true';
    }

    /**
     * Ulozi zvoleny jizdni rad do cookie
     * @param timeTable id jizdniho radu k ulozeni do cookie
     * @param active true, pokud je ukladany jizdni rad aktivni
     */
    storeSelectedTimeTable(timeTable: string, active: boolean): void {
        Cookie.set(UserService.SELECTED_TIME_TABLE_IDENT, timeTable);
        Cookie.set(UserService.SELECTED_TIME_TABLE_ACTIVE, active ? 'true' : 'false');
    }

    /**
     * @returns {string} URI k API dle zvoleneho jizdniho radu
     */
    getApiPrefix(): string {
        let currTimeTable = this.getSelectedTimeTable();
        if (currTimeTable) {
            return AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(currTimeTable) + "/";
        }

        return AppSettings.API_ENDPOINT;
    }

    /**
     * Zobrazi uzivateli na obrazovce informacni banner ze zpravou
     * @param msg zprava
     * @param important true, pokud je zprava dulezita
     * @param longDuration true, pokud se ma zprava zobrazovat dele nez obycejne
     */
    setMsg(msg: string, important?: boolean, longDuration?: boolean) {
        if (this.importantMsg) {
            //pokud je aktualni important tak ji nechci prebit
            this.importantMsg = false;
            return;
        }

        this.msg = msg;
        if (important) this.importantMsg = true;

        //zobrazujeme jen na 3s
        setTimeout(() => {
            this.msg = null;
        }, longDuration ? 5000 : 2500);
    }

}