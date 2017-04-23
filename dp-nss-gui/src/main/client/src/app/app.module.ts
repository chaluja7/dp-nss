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
import {AgenciesComponent} from "./_admin/agency/agencies.component";
import {AdminAgencyService} from "./_service/_admin/admin-agency.service";
import {PagerComponent} from "./_admin/pager/pager.component";
import {AgencyComponent} from "./_admin/agency/agency.component";
import {AgencyNewComponent} from "./_admin/agency/agency-new.component";
import {RoutesComponent} from "./_admin/route/routes.component";
import {AdminRouteService} from "./_service/_admin/admin-route.service";
import {RouteTypePipe} from "./_pipe/route-type.pipe";
import {RouteComponent} from "./_admin/route/route.component";
import {RouteNewComponent} from "./_admin/route/route-new.component";
import {AdminCalendarService} from "./_service/_admin/admin-calendar.service";
import {CalendarsComponent} from "./_admin/calendar/calendars.component";
import {CalendarDatesPipe} from "./_pipe/calendar-dates.pipe";
import {CalendarComponent} from "./_admin/calendar/calendar.component";
import {CalendarNewComponent} from "./_admin/calendar/calendar-new.component";
import {CalendarExceptionTypePipe} from "./_pipe/calendar-exception-type.pipe";
import {AdminShapeService} from "./_service/_admin/admin-shape.service";
import {ShapesComponent} from "./_admin/shape/shapes.component";
import {ShapeComponent} from "./_admin/shape/shape.component";
import {ShapeNewComponent} from "./_admin/shape/shape-new.component";
import {TripsComponent} from "./_admin/trip/trips.component";
import {AdminTripService} from "./_service/_admin/admin-trip.service";
import {TripComponent} from "./_admin/trip/trip.component";
import {TripNewComponent} from "./_admin/trip/trip-new.component";
import {RouteImgComponent} from "./route-img/route-img.component";
import {SearchTripComponent} from "./search/trip/search-trip.component";
import {TripService} from "./_service/trip.service";
import {AgmCoreModule} from "angular2-google-maps/core";
import {SearchStopComponent} from "./search/stop/search-stop.component";
import {SearchTripMapComponent} from "./search/trip/search-trip-map.component";
import {AdminPersonService} from "./_service/_admin/admin-person.service";
import {PersonComponent} from "./_admin/person/person.component";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    TimeTablesComponent,
    TimeTableComponent,
    SearchComponent,
    StopsComponent,
    StopComponent,
    StopNewComponent,
    WheelChairPipe,
    CsNumberPipe,
    AgenciesComponent,
    PagerComponent,
    AgencyComponent,
    AgencyNewComponent,
    RoutesComponent,
    RouteComponent,
    RouteNewComponent,
    RouteTypePipe,
    CalendarsComponent,
    CalendarDatesPipe,
    CalendarComponent,
    CalendarNewComponent,
    CalendarExceptionTypePipe,
    ShapesComponent,
    ShapeComponent,
    ShapeNewComponent,
    TripsComponent,
    TripComponent,
    TripNewComponent,
    RouteImgComponent,
    SearchTripComponent,
    SearchStopComponent,
    SearchTripMapComponent,
    PersonComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    NKDatetimeModule,
    Ng2CompleterModule,
    MdProgressBarModule,
    AgmCoreModule.forRoot({
        apiKey: 'AIzaSyDbG9yEX9XujpyaKn68wG3e4laNNa_roOI'
    })
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
      AdminStopService,
      AdminAgencyService,
      AdminRouteService,
      AdminCalendarService,
      AdminShapeService,
      AdminTripService,
      TripService,
      AdminPersonService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
