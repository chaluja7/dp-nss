import {Component, OnInit} from "@angular/core";
import {TimeTable} from "../_model/time-table";
import {TimeTableService} from "../_service/time-table.service";

@Component({
  moduleId: module.id,
  selector: 'home-component',
  templateUrl: './home.component.html',
  styleUrls: [ './home.component.css' ]
})
export class HomeComponent implements OnInit {

  timeTables: TimeTable[] = [];

  constructor(private timeTableService: TimeTableService) { }

  ngOnInit(): void {
    //this.timeTableService.getTimeTables().then(timeTables => this.timeTables = timeTables.slice(0, 2));
  }

}
