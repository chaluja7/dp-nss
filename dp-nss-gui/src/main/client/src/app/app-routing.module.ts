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
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}