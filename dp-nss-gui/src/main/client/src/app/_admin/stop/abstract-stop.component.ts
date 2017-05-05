import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {Stop} from "../../_model/search-result-model";
import {AdminStopService} from "../../_service/_admin/admin-stop.service";
import {AppSettings} from "../../_common/app.settings";
import {UserService} from "../../_service/user.service";
import {RemoteData, CompleterService} from "ng2-completer";
import {Headers} from "@angular/http";
import {HttpClient} from "../../_service/http-client";

/**
 * Komponenta administrace stanic
 */
@Component({
    moduleId: module.id
})
export abstract class AbstractStopComponent {

    stop: Stop;
    loading = false;
    error = '';
    stopTimesError = '';
    wheelChairOptions = AppSettings.getPossibleWheelChairOptions();
    remoteStops: RemoteData;
    stopSearchQuery: string;
    abstract newRecord: boolean;

    constructor(protected adminStopService: AdminStopService, protected route: ActivatedRoute, protected location: Location,
                protected userService: UserService, protected completerService: CompleterService, protected http: HttpClient) {

        this.remoteStops = completerService.remote(null, 'id,name', 'id,name');
        this.remoteStops.urlFormater(term => {
            return encodeURI(this.userService.getApiPrefix() + `${AdminStopService.STOP_URL}?searchQuery=${term}`)
        });
        let headers = new Headers();
        http.createAuthorizationHeader(headers);
        this.remoteStops.headers(headers);
        this.remoteStops.dataField('');
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

    /**
     * osetri rodicovskou stanici
     */
    protected handleParentStop(): void {
        if (!this.stop.parentStopId) {
            //kvuli tomu, abych tam neposlal undefined
            this.stop.parentStopId = null;
        } else if (this.stop.parentStopId.indexOf(' ') >= 0) {
            //z autocompletu tam vlezlo i jmeno, ale ja chci jen ID
            this.stop.parentStopId = this.stop.parentStopId.split(' ')[0];
        }
    }

}
