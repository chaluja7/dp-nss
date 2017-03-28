import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {TimeTablesComponent} from "./_admin/timeTable/time-tables.component";
import {TimeTableComponent} from "./_admin/timeTable/time-table.component";
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {AuthGuard} from "./_guard/auth.guard";
import {StopsComponent} from "./_admin/stop/stops.component";
import {AuthWithTimeTableGuard} from "./_guard/auth-with-time-table.guard";
import {StopComponent} from "./_admin/stop/stop.component";
import {StopNewComponent} from "./_admin/stop/stop-new.component";
import {AgenciesComponent} from "./_admin/agency/agencies.component";
import {AgencyNewComponent} from "./_admin/agency/agency-new.component";
import {AgencyComponent} from "./_admin/agency/agency.component";
import {RoutesComponent} from "./_admin/route/routes.component";
import {RouteNewComponent} from "./_admin/route/route-new.component";
import {RouteComponent} from "./_admin/route/route.component";
import {CalendarsComponent} from "./_admin/calendar/calendars.component";
import {CalendarComponent} from "./_admin/calendar/calendar.component";
import {CalendarNewComponent} from "./_admin/calendar/calendar-new.component";
import {ShapesComponent} from "./_admin/shape/shapes.component";
import {ShapeComponent} from "./_admin/shape/shape.component";
import {ShapeNewComponent} from "./_admin/shape/shape-new.component";
import {TripsComponent} from "./_admin/trip/trips.component";
import {TripComponent} from "./_admin/trip/trip.component";
import {TripNewComponent} from "./_admin/trip/trip-new.component";
import {SearchTripComponent} from "./search/trip/search-trip.component";
import {SearchStopComponent} from "./search/stop/search-stop.component";
import {SearchTripMapComponent} from "./search/trip/search-trip-map.component";

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'timeTable', component: TimeTablesComponent, canActivate: [AuthGuard] },
  { path: 'timeTable/:id', component: TimeTableComponent, canActivate: [AuthGuard] },
  { path: 'stop', component: StopsComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'stop/create', component: StopNewComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'stop/:id', component: StopComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'agency', component: AgenciesComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'agency/create', component: AgencyNewComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'agency/:id', component: AgencyComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'route', component: RoutesComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'route/create', component: RouteNewComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'route/:id', component: RouteComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'calendar', component: CalendarsComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'calendar/create', component: CalendarNewComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'calendar/:id', component: CalendarComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'shape', component: ShapesComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'shape/create', component: ShapeNewComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'shape/:id', component: ShapeComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'trip', component: TripsComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'trip/create', component: TripNewComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'trip/:id', component: TripComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'search-trip/:schema/:id', component: SearchTripComponent },
  { path: 'search-trip-map/:schema/:id', component: SearchTripMapComponent },
  { path: 'search-stop/:schema/:id', component: SearchStopComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}