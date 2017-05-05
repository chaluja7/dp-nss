import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {AppSettings} from "../../_common/app.settings";
import {AbstractTripComponent} from "./abstract-trip.component";
import {Trip} from "../../_model/trip";

/**
 * Komponenta vytvoreni nove jizdy
 */
@Component({
    moduleId: module.id,
    selector: 'trip-new-component',
    templateUrl: './trip.component.html'
})
export class TripNewComponent extends AbstractTripComponent implements OnInit {

    newRecord = true;

    ngOnInit(): void {
        super.onInit();
        this.trip = new Trip();
    }

    onSubmit(): void {
        this.loading = true;
        this.handleRoute();
        this.handleTimes();

        if (!this.checkTimes()) {
            return;
        }


        this.adminTripService.create(this.trip)
            .subscribe(trip => {
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                    this.goBack()
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                    this.loading = false;
                });
    }

}
