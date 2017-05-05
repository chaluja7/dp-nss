import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Params} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {AbstractStopComponent} from "./abstract-stop.component";

/**
 * Komponenta editace jizdniho radu
 */
@Component({
    moduleId: module.id,
    selector: 'stop-component',
    templateUrl: './stop.component.html'
})
export class StopComponent extends AbstractStopComponent implements OnInit {

    newRecord = false;

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.adminStopService.getStop(params['id']))
            .subscribe(stop => {
                this.stop = stop
            }, err => {
            });
    }

    onSubmit(): void {
        this.loading = true;
        this.handleParentStop();

        this.adminStopService.update(this.stop)
            .subscribe(stop => {
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                    this.goBack()
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                    this.loading = false;
                });
    }

    /**
     * smaze jizdni rad
     */
    doDelete(): void {
        this.adminStopService.delete(this.stop.id)
            .subscribe(() => {
                    this.userService.setMsg(AppSettings.DELETE_SUCCESS);
                    this.goBack()
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                    this.loading = false;
                });

    }

    confirm(msg: string): boolean {
        return confirm(msg);
    }

}
