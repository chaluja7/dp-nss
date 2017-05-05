import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {TimeTable} from "../../_model/time-table";
import {AdminTimeTableService} from "../../_service/_admin/admin-time-table.service";
import {AppSettings} from "../../_common/app.settings";
import {UserService} from "../../_service/user.service";
import {HttpClient} from "../../_service/http-client";

/**
 * Komponenta editace jizdniho radu
 */
@Component({
    moduleId: module.id,
    selector: 'time-table-component',
    templateUrl: './time-table.component.html'
})
export class TimeTableComponent implements OnInit {

    timeTable: TimeTable;
    loading = false;
    error = '';
    fileError = '';
    file: File;

    constructor(private adminTimeTableService: AdminTimeTableService, private route: ActivatedRoute, private location: Location,
                private userService: UserService, private http: HttpClient) {
    }

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.adminTimeTableService.getTimeTable(params['id']))
            .subscribe(timeTable => {
                this.timeTable = timeTable
            }, err => {
            });
    }

    /**
     * odeslani formulare editace
     */
    onSubmit(): void {
        this.loading = true;
        this.adminTimeTableService.update(this.timeTable)
            .subscribe(timeTable => {
                    this.goBack();
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                    this.loading = false;
                });
    }

    /**
     * vrati se zpet v historii
     */
    goBack(): void {
        this.location.back();
    }

    /**
     * provede stazeni jizdniho radu ve formatu GTFS
     */
    downloadGtfs() {
        this.adminTimeTableService.downloadFile()
            .subscribe((response) => {
                    this.http.extractZipContent(response, this.userService.getSelectedTimeTable());
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR + err;
                });
    }

    /**
     * nahraje jizdni rad ve formatu GTFS
     */
    uploadGtfs() {
        this.fileError = null;

        if (!this.file || !this.file.name.endsWith('.zip')) {
            this.fileError = 'Musíte zvolit .zip soubor s jízdními řády ve formátu GTFS.';
            return;
        }

        this.adminTimeTableService.uploadFile('file', this.file)
            .subscribe((response) => {
                    this.timeTable.synchronizing = true;
                    this.userService.storeSelectedTimeTable(this.timeTable.id, false);
                },
                err => {
                    this.fileError = 'Chyba při nahrávání souboru.';
                });
    }

    /**
     * chytne event zmena nahraneho souboru
     * @param event event
     */
    fileChange(event) {
        this.file = null;
        this.fileError = null;
        let fileList: FileList = event.target.files;
        if (fileList.length > 0) {
            this.file = fileList[0];
        }
    }

}
