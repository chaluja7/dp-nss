import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {UserService} from "../../_service/user.service";
import {HttpClient} from "../../_service/http-client";
import {AdminPersonService} from "../../_service/_admin/admin-person.service";
import {Person} from "../../_model/person";
import {ResetPassword} from "../../_model/reset-password";
import {AppSettings} from "../../_common/app.settings";
@Component({
    moduleId: module.id,
    selector: 'person-component',
    templateUrl: './person.component.html'
})
export class PersonComponent implements OnInit {

    person: Person;
    resetPassword: ResetPassword = new ResetPassword();
    loading = false;
    error = '';
    ok = '';

    constructor(private adminPersonService: AdminPersonService, private route: ActivatedRoute, private location: Location,
                private userService: UserService, private http: HttpClient) {
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.adminPersonService.getPerson(params['id']))
            .subscribe(person => {
                this.person = person
            }, err => {
            });
    }

    changePassword(): void {
        if(this.resetPassword.newPassword !== this.resetPassword.newPasswordConfirmation) {
            this.error = 'Hesla se neshodují';
            return;
        }

        this.error = null;
        this.loading = true;
        this.adminPersonService.changePassword(this.person.id, this.resetPassword)
            .subscribe(() => {
                    this.ok = AppSettings.PWD_SUCCESS;
                    this.loading = false;
                    this.resetPassword = new ResetPassword();
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + ' Špatné současné heslo?';
                    this.loading = false;
                    this.resetPassword = new ResetPassword();
                });
    }

    goBack(): void {
        this.location.back();
    }

}
