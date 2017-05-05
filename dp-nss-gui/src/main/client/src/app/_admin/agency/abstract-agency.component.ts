import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {UserService} from "../../_service/user.service";
import {Agency} from "../../_model/agency";
import {AdminAgencyService} from "../../_service/_admin/admin-agency.service";

/**
 * Komponenta administrace dopravcu
 */
@Component({
    moduleId: module.id
})
export abstract class AbstractAgencyComponent {

    agency: Agency;
    loading = false;
    error = '';
    abstract newRecord: boolean;

    constructor(protected adminAgencyService: AdminAgencyService, protected route: ActivatedRoute, protected location: Location,
                protected userService: UserService) {
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
     * vrati se zpet v historii
     */
    goBack(): void {
        this.location.back();
    }

}
