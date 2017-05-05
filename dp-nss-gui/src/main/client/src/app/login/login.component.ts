import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AuthenticationService} from "../_service/authentication.service";
import {User} from "../_model/user";

/**
 * Komponenta prihlaseni (prihlasovaci formular)
 */
@Component({
    moduleId: module.id,
    templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {

    model: User = new User();
    loading = false;
    error = '';

    constructor(private router: Router, private authenticationService: AuthenticationService) {
    }

    ngOnInit() {
        // reset login status
        this.authenticationService.logout();
    }

    /**
     * provede prihlaseni s uzivatelem zadanymi parametry
     */
    login() {
        this.loading = true;
        this.authenticationService.login(this.model.username, this.model.password)
            .subscribe(person => {
                    if (person.passwordChangeRequired) {
                        this.router.navigate(['/person', person.id]);
                    } else {
                        this.router.navigate(['/timeTable']);
                    }
                },
                err => {
                    this.loginFailed();
                });
    }

    /**
     * selhani prihlaseni
     */
    private loginFailed() {
        this.error = 'Neplatné uživatelské jméno nebo heslo.';
        this.loading = false;
    }
}
