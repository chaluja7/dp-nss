import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {TimeTablesComponent} from "./timeTable/time-tables.component";
import {TimeTableComponent} from "./timeTable/time-table.component";
import {HomeComponent} from "./home/home.component";

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'timeTable',     component: TimeTablesComponent },
  { path: 'timeTable/:id', component: TimeTableComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}