import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {UserService} from "../../_service/user.service";
import {AdminPersonService} from "../../_service/_admin/admin-person.service";
import {TimeTableService} from "../../_service/time-table.service";
import {HttpClient} from "../../_service/http-client";
import {Person} from "../../_model/person";
import {LoggedUser} from "../../_model/logged-user";

/**
 * Komponenta administrace osob
 */
@Component({
    moduleId: module.id
})
export abstract class AbstractPersonComponent {

    person: Person;
    availableTimeTables: string[] = [];
    loading = false;
    error = '';
    ok = '';
    currentTimeTables: StringValue[] = [];
    abstract newRecord: boolean;

    constructor(protected adminPersonService: AdminPersonService, protected route: ActivatedRoute, protected location: Location,
                protected userService: UserService, protected timeTableService: TimeTableService, protected http: HttpClient, protected router: Router) {
    }

    //musi zavolat extendujici trida!
    onInit() {
        this.timeTableService.getTimeTables()
            .subscribe(timeTables => {
                for (let timeTable of timeTables) {
                    this.availableTimeTables.push(timeTable.id);
                }
            }, err => {
            });
    }

    /**
     * osetri jizdni rady
     */
    handleTimeTables(): void {
        this.person.timeTables = [];
        for (let timeTable of this.currentTimeTables) {
            this.person.timeTables.push(timeTable.value);
        }
    }

    /**
     * vynuceni onInit na potomku
     */
    abstract ngOnInit(): void;

    /**
     * obsluha odeslani formulare
     */
    abstract onSubmit(): void;

    /**
     * vrati se v historii zpet
     */
    goBack(): void {
        this.location.back();
    }

    /**
     * prida novy jizdni rad k osobe
     */
    addNewTimeTable(): void {
        if (!this.currentTimeTables) this.currentTimeTables = [];
        this.currentTimeTables.push(new StringValue(null));
    }

    /**
     * @returns {LoggedUser} aktualne prihlaseny uzivatel
     */
    getCurrentPerson(): LoggedUser {
        return this.userService.getLoggedUser();
    }

    /**
     * odstrani jizdni rad z indexu pole
     * @param index index pole
     */
    deleteTimeTableItem(index: number) {
        this.currentTimeTables.splice(index, 1);
    }

    confirm(msg: string): boolean {
        return confirm(msg);
    }

}

export class StringValue {
    value: string;

    constructor(value: string) {
        this.value = value;
    }
}
