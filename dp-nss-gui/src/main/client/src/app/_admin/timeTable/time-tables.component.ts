import {Component, OnInit} from "@angular/core";
import {TimeTable} from "../../_model/time-table";
import {AdminTimeTableService} from "../../_service/_admin/admin-time-table.service";
import {Router} from "@angular/router";
import {UserService} from "../../_service/user.service";

/**
 * Komponenta seznamu jizdnich radu
 */
@Component({
    moduleId: module.id,
    selector: 'time-tables-component',
    templateUrl: './time-tables.component.html',
    styleUrls: ['./time-tables.component.css']
})
export class TimeTablesComponent implements OnInit {

    timeTables: TimeTable[];

    selectedTimeTable: TimeTable;

    constructor(private router: Router, private adminTimeTableService: AdminTimeTableService, private userService: UserService) {
    }

    ngOnInit(): void {
        this.getTimeTables();
    }

    /**
     * vrati jizdni rady
     */
    getTimeTables(): void {
        this.adminTimeTableService.getTimeTables()
            .subscribe(timeTables => {
                this.timeTables = timeTables;
                //a vyberu aktualni aktivni jizdni rad, pokud ho uzivatel ma
                let currentTimeTable = this.userService.getSelectedTimeTable();
                if (currentTimeTable) {
                    for (let timeTable of timeTables) {
                        if (timeTable.id === currentTimeTable) {
                            this.selectedTimeTable = timeTable;
                            break;
                        }
                    }
                }

                //pokud neni nyni zadny aktivni, tak udelam aktvni ten prvni
                if (!this.selectedTimeTable && timeTables.length > 0) {
                    this.selectedTimeTable = timeTables[0];
                    this.userService.storeSelectedTimeTable(this.selectedTimeTable.id, !this.selectedTimeTable.synchronizing);
                }
            }, err => {
            });
    }

    /**
     * preskoci na detail jizdniho radu
     * @param timeTable jizdni rad
     */
    goToDetail(timeTable: TimeTable): void {
        this.router.navigate(['/timeTable', timeTable.id]);
    }

    /**
     * chytne udalost zmeny vyberu jizdniho radu a ulozi jej do cookie
     * @param timeTable jizdni rad
     */
    onSelect(timeTable: TimeTable): void {
        this.selectedTimeTable = timeTable;
        this.userService.storeSelectedTimeTable(timeTable.id, !timeTable.synchronizing);
    }

}
