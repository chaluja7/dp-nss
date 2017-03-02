import {Injectable} from "@angular/core";
import {TimeTable} from "./time-table";
import {TIME_TABLES} from "./mock-time-tables";

@Injectable()
export class TimeTableService {

  getTimeTables(): Promise<TimeTable[]> {
    return Promise.resolve(TIME_TABLES);
  }

  getTimeTable(id: string): Promise<TimeTable> {
    return this.getTimeTables().then(timeTables => timeTables.find(timeTable => timeTable.id === id));
  }

}