import {Component, OnInit} from "@angular/core";
import {TimeTable} from "../timeTable/time-table";
import {TimeTableService} from "../timeTable/time-table.service";

@Component({
  selector: 'home-component',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  timeTables: TimeTable[];
  selectedTimeTable: TimeTable;

  constructor(private timeTableService: TimeTableService) { }

  ngOnInit(): void {
    this.getTimeTables();
  }

  getTimeTables(): void {
    this.timeTableService.getTimeTables().then(timeTables => this.timeTables = timeTables);
  }

  onSelect(timeTable: TimeTable): void {
    this.selectedTimeTable = timeTable;
  }

}
