import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Params} from "@angular/router";
import {ResetPassword} from "../../_model/reset-password";
import {AppSettings} from "../../_common/app.settings";
import {AbstractPersonComponent, StringValue} from "./abstract-person.component";
@Component({
    moduleId: module.id,
    selector: 'person-component',
    templateUrl: './person.component.html'
})
export class PersonComponent extends AbstractPersonComponent implements OnInit {

    resetPassword: ResetPassword = new ResetPassword();
    newRecord = false;
    canBeDeleted: boolean;

    ngOnInit(): void {
        super.onInit();

        this.route.params
            .switchMap((params: Params) => this.adminPersonService.getPerson(params['id']))
            .subscribe(person => {
                this.person = person;
                this.canBeDeleted = this.getCurrentPerson().id !== person.id;

                for(let role of person.roles) {
                    if(role === 'ADMIN') {
                        this.person.isAdmin = true;
                        break;
                    }
                }

                for(let timeTable of person.timeTables) {
                    this.currentTimeTables.push(new StringValue(timeTable));
                }
            }, err => {});
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

    onSubmit(): void {
        this.loading = true;

        this.handleTimeTables();
        this.adminPersonService.update(this.person)
            .subscribe(person => {
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                    this.goBack()
                },
                err  => {
                    this.error = AppSettings.SAVE_ERROR;
                    this.loading = false;
                });
    }

    doDelete(): void {
        this.adminPersonService.delete(this.person.id)
            .subscribe(() => {
                    this.userService.setMsg(AppSettings.DELETE_SUCCESS);
                    this.goBack()
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR;
                    this.loading = false;
                });
    }

}
