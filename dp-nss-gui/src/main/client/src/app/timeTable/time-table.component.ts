import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {TimeTable} from "./time-table";
import {TimeTableService} from "./time-table.service";
@Component({
  moduleId: module.id,
  selector: 'time-table-component',
  templateUrl: './time-table.component.html',
  styleUrls: [ './time-table.component.css' ]
})
export class TimeTableComponent implements OnInit {

  timeTable: TimeTable;

  constructor(
      private timeTableService: TimeTableService,
      private route: ActivatedRoute,
      private location: Location
  ) {}

  ngOnInit(): void {
    this.route.params
        .switchMap((params: Params) => this.timeTableService.getTimeTable(params['id']))
        .subscribe(timeTable => this.timeTable = timeTable);
  }

  save(): void {
    this.timeTableService.update(this.timeTable)
        .then(() => this.goBack());
  }

  goBack(): void {
    this.location.back();
  }

}
