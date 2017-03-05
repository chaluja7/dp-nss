import {Component, OnInit} from "@angular/core";
import {TimeTable} from "../timeTable/time-table";
import {TimeTableService} from "../timeTable/time-table.service";
import {SearchModel} from "./search-model";
import {DateService} from "../common/date.service";
import {SearchService} from "./search.service";
import {SearchResultModel} from "./search-result-model";
import {CompleterService, RemoteData} from "ng2-completer";
import {StopService} from "../stop/stop.service";
import {AppSettings} from "../common/app.settings";

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

  private remoteStopsFrom: RemoteData;

  private remoteStopsTo: RemoteData;

  private stopSearchTerm: string;

  constructor(private timeTableService: TimeTableService, public dateService: DateService,
              private searchService: SearchService, private completerService: CompleterService, private stopService: StopService) {

    this.remoteStopsFrom = completerService.remote(null, null, "");
    this.remoteStopsFrom.urlFormater(term => {return this.stopSearchTerm + `${term}`;});
    this.remoteStopsFrom.dataField("");

    //opravdu to musi byt zduplikovane, jinak mi nebudou obe pole fungovat spravne
    this.remoteStopsTo = completerService.remote(null, null, "");
    this.remoteStopsTo.urlFormater(term => {return this.stopSearchTerm + `${term}`;});
    this.remoteStopsTo.dataField("");
  }

  ngOnInit(): void {
    this.timeTableService.getTimeTables().then(
        timeTables => {
          this.timeTables = timeTables;
          // this.searchModel.timeTableId = timeTables.length > 0 ? timeTables[0].id : null;
          //TODO to nahore plati!
          this.searchModel.timeTableId = 'pid';
          this.stopSearchTerm = SearchComponent.getStopSearchTerm(this.searchModel.timeTableId);
        }
    );

    this.searchModel.maxNumOfTransfers = 2;
    this.searchModel.date = new Date();
    this.searchModel.time = new Date();
    this.searchModel.time.setSeconds(0, 0);

    //TODO delete
    this.searchModel.date.setMonth(1);
    this.searchModel.date.setDate(2);
    this.searchModel.time.setHours(9);
    this.searchModel.time.setMinutes(0);

    this.searchModel.stopFrom = 'Dejvická';
    this.searchModel.stopTo = 'Karlovo náměstí';
  }

  onSubmit() : void {
    this.searchResults = null;
    this.submitted = true;
    this.searchService.search(this.searchModel.timeTableId, this.searchModel.stopFrom, this.searchModel.stopTo,
        this.searchModel.date, this.searchModel.time, this.searchModel.maxNumOfTransfers)
        .then(searchResults => {
          this.submitted = false;
          this.searchResults = searchResults;
        })
        .catch(e => this.submitted = false);
  }

  onTimeTableChange() : void {
    this.stopSearchTerm = SearchComponent.getStopSearchTerm(this.searchModel.timeTableId);
  }

  private static getStopSearchTerm(timeTableId: string) : string {
    return AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(timeTableId) + "/stop?startsWith=";
  }

}
