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
import {TimeTableService} from "../../_service/time-table.service";
import {LoggedUser} from "../../_model/logged-user";
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
    currentTimeTables: StringValue[] = [];
    availableTimeTables : string[] = [];

    constructor(private adminPersonService: AdminPersonService, private route: ActivatedRoute, private location: Location,
                private userService: UserService, private timeTableService: TimeTableService, private http: HttpClient) {
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.adminPersonService.getPerson(params['id']))
            .subscribe(person => {
                this.person = person;

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

        this.timeTableService.getTimeTables()
            .subscribe(timeTables => {
                for(let timeTable of timeTables) {
                    this.availableTimeTables.push(timeTable.id);
                }
            }, err  => {});
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

    handleTimeTables(): void {
        this.person.timeTables = [];
        for(let timeTable of this.currentTimeTables) {
            this.person.timeTables.push(timeTable.value);
        }
    }

    getCurrentPerson(): LoggedUser {
        return this.userService.getLoggedUser();
    }

    addNewTimeTable(): void {
        if(!this.currentTimeTables) this.currentTimeTables = [];
        this.currentTimeTables.push(new StringValue(null));
    }

    deleteTimeTableItem(index: number) {
        this.currentTimeTables.splice(index, 1);
    }

    confirm(msg: string): boolean {
        return confirm(msg);
    }

    goBack(): void {
        this.location.back();
    }

}

export class StringValue {
    value: string;

    constructor(value: string) {
        this.value = value;
    }
}
