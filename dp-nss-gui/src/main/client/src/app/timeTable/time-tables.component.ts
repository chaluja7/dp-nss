import {Component, OnInit} from "@angular/core";
import {TimeTable} from "./time-table";
import {TimeTableService} from "./time-table.service";
import {Router} from "@angular/router";

@Component({
  moduleId: module.id,
  selector: 'time-tables-component',
  templateUrl: './time-tables.component.html',
  styleUrls: ['./time-tables.component.css']
})
export class TimeTablesComponent implements OnInit {

  timeTables: TimeTable[];

  newTimeTable: TimeTable = new TimeTable;

  constructor(private router: Router, private timeTableService: TimeTableService) { }

  ngOnInit(): void {
    this.getTimeTables();
  }

  getTimeTables(): void {
    this.timeTableService.getTimeTables().then(timeTables => this.timeTables = timeTables);
  }

  goToDetail(timeTable: TimeTable): void {
    this.router.navigate(['/timeTable', timeTable.id]);
  }

  add(): void {
    this.newTimeTable.id = this.newTimeTable.id.trim();
    this.newTimeTable.name = this.newTimeTable.name.trim();

    if (!this.newTimeTable.id || !this.newTimeTable.name) { return; }
    this.timeTableService.create(this.newTimeTable)
        .then(timeTable => {
          this.timeTables.push(timeTable);
          this.newTimeTable = new TimeTable;
        });
  }

  delete(timeTable: TimeTable): void {
    this.timeTableService
        .delete(timeTable.id)
        .then(() => {
          this.timeTables = this.timeTables.filter(t => t !== timeTable);
        });
  }

}
