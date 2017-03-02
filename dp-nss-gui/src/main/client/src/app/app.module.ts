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

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    TimeTablesComponent,
    TimeTableComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [
      TimeTableService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
