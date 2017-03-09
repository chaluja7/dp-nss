import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {Location} from "@angular/common";
import {TimeTable} from "../../_model/time-table";
import {AdminTimeTableService} from "../../_service/_admin/admin-time-table.service";
@Component({
  moduleId: module.id,
  selector: 'time-table-component',
  templateUrl: './time-table.component.html',
  styleUrls: [ './time-table.component.css' ]
})
export class TimeTableComponent implements OnInit {

  timeTable: TimeTable;

  constructor(
      private adminTimeTableService: AdminTimeTableService,
      private route: ActivatedRoute,
      private location: Location
  ) {}

  ngOnInit(): void {
    this.route.params
        .switchMap((params: Params) => this.adminTimeTableService.getTimeTable(params['id']))
        .subscribe(timeTable => {this.timeTable = timeTable}, err  => {});
  }

  save(): void {
    this.adminTimeTableService.update(this.timeTable)
        .subscribe(timeTable => {this.goBack()}, err  => {});
  }

  goBack(): void {
    this.location.back();
  }

}
