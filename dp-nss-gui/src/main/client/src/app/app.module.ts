import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {HomeComponent} from "./home/home.component";
import {TimeTablesComponent} from "./timeTable/time-tables.component";
import {TimeTableComponent} from "./timeTable/time-table.component";
import {TimeTableService} from "./timeTable/time-table.service";
import {AppRoutingModule} from "./app-routing.module";
import {StopService} from "./stop/stop.service";
import {DateService} from "./common/date.service";
import {SearchService} from "./search/search.service";
import {ErrorService} from "./common/error.service";
import {StopSearchComponent} from "./stop/stop-search.component";
import {SearchComponent} from "./search/search.component";
import {NKDatetimeModule} from "ng2-datetime/ng2-datetime";
import {Ng2CompleterModule} from "ng2-completer";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    TimeTablesComponent,
    TimeTableComponent,
    StopSearchComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    NKDatetimeModule,
    Ng2CompleterModule
  ],
  providers: [
      DateService,
      ErrorService,
      TimeTableService,
      StopService,
      SearchService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
