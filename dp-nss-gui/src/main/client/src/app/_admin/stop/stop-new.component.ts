import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Stop} from "../../_model/search-result-model";
import {AppSettings} from "../../_common/app.settings";
import {AbstractStopComponent} from "./abstract-stop.component";

/**
 * Komponenta vytvoreni nove stanice
 */
@Component({
    moduleId: module.id,
    selector: 'stop-new-component',
    templateUrl: './stop.component.html'
})
export class StopNewComponent extends AbstractStopComponent implements OnInit {

    newRecord = true;

    ngOnInit(): void {
        this.stop = new Stop();
    }

    onSubmit(): void {
        this.loading = true;
        this.handleParentStop();

        this.adminStopService.create(this.stop)
            .subscribe(stop => {
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                    this.goBack()
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                    this.loading = false;
                });
    }

}
