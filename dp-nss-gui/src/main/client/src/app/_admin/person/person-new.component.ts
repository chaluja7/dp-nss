import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {AbstractPersonComponent} from "./abstract-person.component";
import {Person} from "../../_model/person";
import {AppSettings} from "../../_common/app.settings";

/**
 * Komponenta vytvoreni nove osoby
 */
@Component({
    moduleId: module.id,
    selector: 'person-new-component',
    templateUrl: './person.component.html'
})
export class PersonNewComponent extends AbstractPersonComponent implements OnInit {

    newRecord = true;

    ngOnInit(): void {
        super.onInit();
        this.person = new Person();

        let roles: string[] = [];
        roles.push("USER");
        this.person.roles = roles;
    }

    onSubmit(): void {
        this.loading = true;

        this.handleTimeTables();

        this.adminPersonService.create(this.person)
            .subscribe(person => {
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS + ' Poznamenejte si jednorázové heslo.');
                    this.person = person;
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + ' Neexistuje již uživatel se stejným uživatelským jménem?';
                    this.loading = false;
                });

    }

}
