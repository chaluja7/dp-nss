import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {UserService} from "../../_service/user.service";
import {AdminPersonService} from "../../_service/_admin/admin-person.service";
import {TimeTableService} from "../../_service/time-table.service";
import {HttpClient} from "../../_service/http-client";
import {Person} from "../../_model/person";
import {LoggedUser} from "../../_model/logged-user";
@Component({
  moduleId: module.id
})
export abstract class AbstractPersonComponent {

  person: Person;
  availableTimeTables : string[] = [];
  loading = false;
  error = '';
  ok = '';
  currentTimeTables: StringValue[] = [];
  abstract newRecord: boolean;

  constructor(protected adminPersonService: AdminPersonService, protected route: ActivatedRoute, protected location: Location,
              protected userService: UserService, protected timeTableService: TimeTableService, protected http: HttpClient) {}

  //musi zavolat extendujici trida!
  onInit() {
    this.timeTableService.getTimeTables()
        .subscribe(timeTables => {
          for(let timeTable of timeTables) {
            this.availableTimeTables.push(timeTable.id);
          }
        }, err  => {});
  }

  handleTimeTables(): void {
    this.person.timeTables = [];
    for(let timeTable of this.currentTimeTables) {
      this.person.timeTables.push(timeTable.value);
    }
  }

  abstract ngOnInit(): void;

  abstract onSubmit(): void;

  goBack(): void {
    this.location.back();
  }

  addNewTimeTable(): void {
    if(!this.currentTimeTables) this.currentTimeTables = [];
    this.currentTimeTables.push(new StringValue(null));
  }

  getCurrentPerson(): LoggedUser {
    return this.userService.getLoggedUser();
  }

  deleteTimeTableItem(index: number) {
    this.currentTimeTables.splice(index, 1);
  }

  confirm(msg: string): boolean {
    return confirm(msg);
  }

}

export class StringValue {
  value: string;

  constructor(value: string) {
    this.value = value;
  }
}
