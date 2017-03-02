import {Component, Input} from "@angular/core";
import {TimeTable} from "./time-table";

@Component({
  selector: 'time-table-component',
  templateUrl: './time-table.component.html'
})
export class TimeTableComponent {
  @Input() timeTable: TimeTable;
}
