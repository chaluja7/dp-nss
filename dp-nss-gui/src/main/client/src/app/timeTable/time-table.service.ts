import {Injectable} from "@angular/core";
import {TimeTable} from "./time-table";
import {TIME_TABLES} from "./mock-time-tables";

@Injectable()
export class TimeTableService {

  getTimeTables(): Promise<TimeTable[]> {
    return Promise.resolve(TIME_TABLES);
  }

}