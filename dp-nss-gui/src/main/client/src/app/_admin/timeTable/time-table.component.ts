import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {TimeTable} from "../../_model/time-table";
import {AdminTimeTableService} from "../../_service/_admin/admin-time-table.service";
import {AppSettings} from "../../_common/app.settings";
import {UserService} from "../../_service/user.service";
import {HttpClient} from "../../_service/http-client";
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

    onSubmit(): void {
        this.loading = true;
        this.adminTimeTableService.update(this.timeTable)
            .subscribe(timeTable => {
                    this.goBack();
                    this.userService.setMsg(AppSettings.SAVE_SUCCESS);
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR;
                    this.loading = false;
                });
    }

    goBack(): void {
        this.location.back();
    }

    downloadGtfs() {
        this.adminTimeTableService.downloadFile()
            .subscribe((response) => {
                    this.http.extractZipContent(response, this.userService.getSelectedTimeTable());
                },
                err => {
                    this.error = AppSettings.SAVE_ERROR;
                });
    }

    uploadGtfs() {
        this.fileError = null;

        if(!this.file || !this.file.name.endsWith('.zip')) {
            this.fileError = 'Musíte zvolit .zip soubor s jízdními řády ve formátu GTFS.';
            return;
        }

        this.adminTimeTableService.uploadFile('file', this.file)
            .subscribe((response) => {
                    console.log(response);
                },
                err => {
                    this.fileError = 'Chyba při nahrávání souboru.';
                });
    }

    fileChange(event) {
        this.file = null;
        this.fileError = null;
        let fileList: FileList = event.target.files;
        if(fileList.length > 0) {
            this.file = fileList[0];
        }
    }

}
