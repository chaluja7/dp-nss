import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {AppSettings} from "../../_common/app.settings";
import {AbstractAgencyComponent} from "./abstract-agency.component";
import {Agency} from "../../_model/agency";

/**
 * Komponenta vlozeni noveho dopravce
 */
@Component({
    moduleId: module.id,
    selector: 'agency-new-component',
    templateUrl: './agency.component.html'
})
export class AgencyNewComponent extends AbstractAgencyComponent implements OnInit {

    newRecord = true;

    ngOnInit(): void {
        this.agency = new Agency();
    }

    onSubmit(): void {
        this.loading = true;

        this.adminAgencyService.create(this.agency)
            .subscribe(agency => {
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                    this.goBack()
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                    this.loading = false;
                });
    }

}
