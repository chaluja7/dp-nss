import {Component, OnInit} from "@angular/core";
import {TimeTable} from "../_model/time-table";
import {TimeTableService} from "../_service/time-table.service";
import {SearchModel} from "../_model/search-model";
import {DateService} from "../_service/date.service";
import {SearchService} from "../_service/search.service";
import {SearchResultModel} from "../_model/search-result-model";
import {CompleterService, RemoteData} from "ng2-completer";
import {StopService} from "../_service/stop.service";
import {AppSettings} from "../_common/app.settings";

@Component({
  moduleId: module.id,
  selector: 'search-component',
  templateUrl: './search.component.html',
  styleUrls: [ './search.component.css' ]
})
export class SearchComponent implements OnInit {


  private static SEARCH_ERROR = 'Chyba - ';

  searchModel: SearchModel;

  searchModelClone: SearchModel;

  timeTables: TimeTable[] = [];

  numOfTransfers: number[] = [0, 1, 2, 3, 4];

  submitted = false;

  searchResults: SearchResultModel[];

  private remoteStopsFrom: RemoteData;

  private remoteStopsTo: RemoteData;

  private remoteStopsThrough: RemoteData;

  private stopSearchTerm: string;

  showThroughStop: boolean;

  error = '';

  noMoreNextResults: boolean;

  noMorePrevResults: boolean;

  constructor(private timeTableService: TimeTableService, public dateService: DateService,
              private searchService: SearchService, private completerService: CompleterService, private stopService: StopService) {

    this.searchModel = new SearchModel();
    this.searchModel.dateType = 'departure';

    this.remoteStopsFrom = completerService.remote(null, null, "");
    this.remoteStopsFrom.urlFormater(term => {return encodeURI(this.stopSearchTerm + `${term}`)});
    this.remoteStopsFrom.dataField("");

    //opravdu to musi byt zduplikovane, jinak mi nebudou obe pole fungovat spravne
    this.remoteStopsTo = completerService.remote(null, null, "");
    this.remoteStopsTo.urlFormater(term => {return encodeURI(this.stopSearchTerm + `${term}`)});
    this.remoteStopsTo.dataField("");

    //opravdu to musi byt zduplikovane, jinak mi nebudou obe pole fungovat spravne
    this.remoteStopsThrough = completerService.remote(null, null, "");
    this.remoteStopsThrough.urlFormater(term => {return encodeURI(this.stopSearchTerm + `${term}`)});
    this.remoteStopsThrough.dataField("");
  }

  ngOnInit(): void {
    this.timeTableService.getTimeTables()
        .subscribe(timeTables => {
              this.timeTables = timeTables;
              this.searchModel.timeTableId = timeTables.length > 0 ? timeTables[0].id : null;
              this.stopSearchTerm = SearchComponent.getStopSearchTerm(this.searchModel.timeTableId);
            },
            err  => {});

    this.searchModel.maxNumOfTransfers = 3;
    this.searchModel.date = new Date();
    this.searchModel.time = new Date();
    this.searchModel.time.setSeconds(0, 0);
  }

  onSubmit() : void {
    this.searchResults = null;
    this.submitted = true;
    this.error = '';
    this.copySearchModel();
    this.searchModel.byArrival = this.searchModel.dateType === 'arrival';
    this.searchService.search(this.searchModel)
        .subscribe(searchResults => {
                this.submitted = false;
                this.searchResults = searchResults;
            },
            err  => {
              this.error = SearchComponent.SEARCH_ERROR + err;
              this.submitted = false;
            });
  }

  showNextResults(): void {
    this.noMoreNextResults = false;
    this.submitted = true;
    this.error = '';

    let lastResult = this.searchResults[this.searchResults.length - 1];
    //datum departure posledniho nalezeneho vysledku
    let lastDepartureDate = DateService.getDateObjectFromString(lastResult.departureDate);
    //cas departure posledniho nalezeneho vysledku
    let lastDepartureTime = DateService.getTimeObjectFromString(lastResult.stopTimes[0].departure);

    lastDepartureTime.setDate(lastDepartureDate.getDate());
    lastDepartureTime.setMonth(lastDepartureDate.getMonth());
    lastDepartureTime.setFullYear(lastDepartureDate.getFullYear());

    //a zvysim cas o jednu minutu, at hledam opravdu az dalsi spoje
    let prevDay = lastDepartureTime.getDay();
    lastDepartureTime.setMinutes(lastDepartureTime.getMinutes() + 1);
      //pokud jsem presel pres pulnoc, tak zvysit o den i datum
    if(prevDay != lastDepartureTime.getDay()) {
        lastDepartureDate.setHours(24);
    }

    this.searchModelClone.date = lastDepartureDate;
    this.searchModelClone.time = lastDepartureTime;
    this.searchModelClone.byArrival = false;

      this.searchService.search(this.searchModelClone)
          .subscribe(searchResults => {
                  this.submitted = false;
                  if(searchResults.length == 0) {
                      this.noMoreNextResults = true;
                  }

                  for(let s of searchResults) {
                      this.searchResults.push(s);
                  }

              },
              err  => {
                  this.error = SearchComponent.SEARCH_ERROR + err;
                  this.submitted = false;
              });
  }

  showPrevResults(): void {
      this.noMorePrevResults = false;
      this.submitted = true;
      this.error = '';

      let firstResult = this.searchResults[0];
      //datum arrival prvniho nalezeneho vysledku
      let firstArrivalDate = DateService.getDateObjectFromString(firstResult.arrivalDate);
      //cas arrival prvniho nalezeneho vysledku
      let firstArrivalTime = DateService.getTimeObjectFromString(firstResult.stopTimes[firstResult.stopTimes.length - 1].arrival);

      firstArrivalTime.setDate(firstArrivalDate.getDate());
      firstArrivalTime.setMonth(firstArrivalDate.getMonth());
      firstArrivalTime.setFullYear(firstArrivalDate.getFullYear());

      //a snizim cas o jednu minutu, at hledam opravdu az predchozi spoje
      let prevDay = firstArrivalTime.getDay();
      firstArrivalTime.setMinutes(firstArrivalTime.getMinutes() - 1);
      //pokud jsem presel pres pulnoc, tak snizit o den i datum
      if(prevDay != firstArrivalTime.getDay()) {
          firstArrivalDate.setHours(-1);
      }

      this.searchModelClone.date = firstArrivalDate;
      this.searchModelClone.time = firstArrivalTime;
      this.searchModelClone.byArrival = true;

      this.searchService.search(this.searchModelClone)
          .subscribe(searchResults => {
                  this.submitted = false;
                  if(searchResults.length == 0) {
                      this.noMorePrevResults = true;
                  } else {
                      //musim vytvorit nove pole kam dam prve tyto nalezen vysledky a pak do nej nakopiruju jiz drive nalezene
                      //pri pouziti splice bych totiz musel delat deepcopy kazdeho prvku v this.searchResults
                      let newSearchResults: SearchResultModel[] = [];
                      for (let s of searchResults) {
                          newSearchResults.push(s);
                      }

                      for (let s of this.searchResults) {
                          newSearchResults.push(s);
                      }

                      this.searchResults = newSearchResults;
                  }
              },
              err  => {
                  this.error = SearchComponent.SEARCH_ERROR + err;
                  this.submitted = false;
              });
  }

  onTimeTableChange() : void {
    this.stopSearchTerm = SearchComponent.getStopSearchTerm(this.searchModel.timeTableId);
  }

  swapFromAndTo(): void {
    let tmp = this.searchModel.stopFrom;
    this.searchModel.stopFrom = this.searchModel.stopTo;
    this.searchModel.stopTo = tmp;
  }

  toogleThroughStop(): void {
    this.searchModel.stopThrough = null;
    this.showThroughStop = !this.showThroughStop;
  }

  private static getStopSearchTerm(timeTableId: string) : string {
    return AppSettings.API_ENDPOINT + AppSettings.getSchemaUrlParam(timeTableId) + "/stop?startsWith=";
  }

  private copySearchModel(): void {
      this.searchModelClone = SearchComponent.createCopy(this.searchModel);
      this.searchModelClone.date = new Date(this.searchModelClone.date);
      this.searchModelClone.time = new Date(this.searchModelClone.time);
  }

    static createCopy(objectToCopy: SearchModel): SearchModel {
        return (JSON.parse(JSON.stringify(objectToCopy)));
    }

}
