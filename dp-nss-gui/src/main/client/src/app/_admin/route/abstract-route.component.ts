import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {Route} from "../../_model/search-result-model";
import {AppSettings} from "../../_common/app.settings";
import {UserService} from "../../_service/user.service";
import {AdminRouteService} from "../../_service/_admin/admin-route.service";
import {Agency} from "../../_model/agency";
import {AdminAgencyService} from "../../_service/_admin/admin-agency.service";

/**
 * Komponenta administrace spoje
 */
@Component({
    moduleId: module.id
})
export abstract class AbstractRouteComponent {

    routeObject: Route;
    loading = false;
    error = '';
    routeTypeOptions = AppSettings.getPossibleRouteTypesOptions();
    agenciesForSelectBox: Agency[];
    abstract newRecord: boolean;

    constructor(protected adminRouteService: AdminRouteService, protected route: ActivatedRoute, protected location: Location,
                protected userService: UserService, protected adminAgencyService: AdminAgencyService) {
    }

    //musi zavolat extendujici trida!
    onInit() {
        this.adminAgencyService.getAgenciesForSelectBox()
            .subscribe(agencies => {
                this.agenciesForSelectBox = agencies;
            }, err => {
            });
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
