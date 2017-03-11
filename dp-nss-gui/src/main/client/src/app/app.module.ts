import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {HomeComponent} from "./home/home.component";
import {TimeTablesComponent} from "./_admin/timeTable/time-tables.component";
import {TimeTableComponent} from "./_admin/timeTable/time-table.component";
import {TimeTableService} from "./_service/time-table.service";
import {AdminTimeTableService} from "./_service/_admin/admin-time-table.service";
import {AppRoutingModule} from "./app-routing.module";
import {StopService} from "./_service/stop.service";
import {DateService} from "./_service/date.service";
import {SearchService} from "./_service/search.service";
import {ErrorService} from "./_service/error.service";
import {StopSearchComponent} from "./stop/stop-search.component";
import {SearchComponent} from "./search/search.component";
import {NKDatetimeModule} from "ng2-datetime/ng2-datetime";
import {Ng2CompleterModule} from "ng2-completer";
import {LoginComponent} from "./login/login.component";
import {AuthenticationService} from "./_service/authentication.service";
import {AuthGuard} from "./_guard/auth.guard";
import {AuthAdminGuard} from "./_guard/auth-admin.guard";
import {HttpClient} from "./_service/http-client";
import {UserService} from "./_service/user.service";
import {MdProgressBarModule} from "@angular2-material/progress-bar";
import {StopsComponent} from "./_admin/stop/stops.component";
import {AuthWithTimeTableGuard} from "./_guard/auth-with-time-table.guard";
import {AdminStopService} from "./_service/_admin/admin-stop.service";
import {WheelChairPipe} from "./_pipe/wheel-chair.pipe";
import {PagerService} from "./_service/_admin/pager.service";
import {CsNumberPipe} from "./_pipe/cs-number.pipe";
import {StopComponent} from "./_admin/stop/stop.component";
import {StopNewComponent} from "./_admin/stop/stop-new.component";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    TimeTablesComponent,
    TimeTableComponent,
    StopSearchComponent,
    SearchComponent,
    StopsComponent,
    StopComponent,
    StopNewComponent,
    WheelChairPipe,
    CsNumberPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    NKDatetimeModule,
    Ng2CompleterModule,
    MdProgressBarModule
  ],
  providers: [
      UserService,
      PagerService,
      HttpClient,
      AuthenticationService,
      AuthGuard,
      AuthAdminGuard,
      AuthWithTimeTableGuard,
      DateService,
      ErrorService,
      TimeTableService,
      StopService,
      SearchService,
      AdminTimeTableService,
      AdminStopService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
