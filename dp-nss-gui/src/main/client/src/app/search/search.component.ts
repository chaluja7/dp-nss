import {Component, OnInit} from "@angular/core";
import {TimeTable} from "../timeTable/time-table";
import {TimeTableService} from "../timeTable/time-table.service";
import {SearchModel} from "./search-model";
import {DateService} from "../common/date.service";
import {SearchService} from "./search.service";
import {SearchResultModel} from "./search-result-model";

@Component({
  moduleId: module.id,
  selector: 'search-component',
  templateUrl: './search.component.html',
  styleUrls: [ './search.component.css' ]
})
export class SearchComponent implements OnInit {

  searchModel: SearchModel = new SearchModel();

  timeTables: TimeTable[] = [];

  numOfTransfers: number[] = [0, 1, 2, 3, 4];

  submitted = false;

  searchResults: SearchResultModel[];

  constructor(private timeTableService: TimeTableService, public dateService: DateService,
              private searchService: SearchService) { }

  ngOnInit(): void {
    this.timeTableService.getTimeTables().then(
        timeTables => {
          this.timeTables = timeTables;
          // this.searchModel.timeTableId = timeTables.length > 0 ? timeTables[0].id : null;
          //TODO to nahore plati!
          this.searchModel.timeTableId = 'pid';
        }
    );

    this.searchModel.maxNumOfTransfers = 2;
    this.searchModel.date = new Date();
    this.searchModel.date.setSeconds(0, 0);

    //TODO delete
    this.searchModel.date.setMonth(1);
    this.searchModel.date.setDate(2);
    this.searchModel.date.setHours(9);
    this.searchModel.date.setMinutes(0);

    this.searchModel.timeTableId = 'pid';
    this.searchModel.stopFrom = 'Dejvická';
    this.searchModel.stopTo = 'Karlovo náměstí';
  }

  onSubmit() : void {
    this.searchResults = null;
    this.submitted = true;
    this.searchService.search(this.searchModel.timeTableId, this.searchModel.stopFrom, this.searchModel.stopTo,
        this.searchModel.date, this.searchModel.maxNumOfTransfers)
        .then(searchResults => {
          this.submitted = false;
          this.searchResults = searchResults;
        })
        .catch(e => this.submitted = false);
  }

}
