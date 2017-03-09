import {Component, OnInit} from "@angular/core";
import {TimeTable} from "../../_model/time-table";
import {AdminTimeTableService} from "../../_service/_admin/admin-time-table.service";
import {Router} from "@angular/router";
import {UserService} from "../../_service/user.service";

@Component({
  moduleId: module.id,
  selector: 'time-tables-component',
  templateUrl: './time-tables.component.html',
  styleUrls: ['./time-tables.component.css']
})
export class TimeTablesComponent implements OnInit {

  timeTables: TimeTable[];

  selectedTimeTable: TimeTable;

  constructor(private router: Router, private adminTimeTableService: AdminTimeTableService, private userService: UserService) { }

  ngOnInit(): void {
    this.getTimeTables();
  }

  getTimeTables(): void {
    this.adminTimeTableService.getTimeTables()
        .subscribe(timeTables => {
          this.timeTables = timeTables;
          //a vyberu aktualni aktivni jizdni rad, pokud ho uzivatel ma
          let currentTimeTable = this.userService.getSelectedTimeTable();
          if(currentTimeTable) {
            for(let timeTable of timeTables) {
              if(timeTable.id === currentTimeTable) {
                this.selectedTimeTable = timeTable;
                break;
              }
            }
          }

          //pokud neni nyni zadny aktivni, tak udelam aktvni ten prvni
          if(!this.selectedTimeTable && timeTables.length > 0) {
            this.selectedTimeTable = timeTables[0];
          }
        }, err  => {});
  }

  goToDetail(timeTable: TimeTable): void {
    this.router.navigate(['/timeTable', timeTable.id]);
  }

  onSelect(timeTable: TimeTable): void {
    this.selectedTimeTable = timeTable;
    this.userService.storeSelectedTimeTable(timeTable.id);
  }

  // add(): void {
  //   this.newTimeTable.id = this.newTimeTable.id.trim();
  //   this.newTimeTable.name = this.newTimeTable.name.trim();
  //
  //   if (!this.newTimeTable.id || !this.newTimeTable.name) { return; }
  //   this.adminTimeTableService.create(this.newTimeTable)
  //       .then(timeTable => {
  //         this.timeTables.push(timeTable);
  //         this.newTimeTable = new TimeTable;
  //       });
  // }
  //
  // delete(timeTable: TimeTable): void {
  //   this.adminTimeTableService
  //       .delete(timeTable.id)
  //       .then(() => {
  //         this.timeTables = this.timeTables.filter(t => t !== timeTable);
  //       });
  // }

}
