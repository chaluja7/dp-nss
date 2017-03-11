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

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'timeTable', component: TimeTablesComponent, canActivate: [AuthGuard] },
  { path: 'timeTable/:id', component: TimeTableComponent, canActivate: [AuthGuard] },
  { path: 'stop', component: StopsComponent, canActivate: [AuthWithTimeTableGuard] },
  { path: 'stop/:id', component: StopComponent, canActivate: [AuthWithTimeTableGuard] },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}