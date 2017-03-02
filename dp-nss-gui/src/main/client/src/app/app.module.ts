import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {HomeComponent} from "./home/home.component";
import {SearchFormComponent} from "./home/search-form.component";
import {TimeTableComponent} from "./timeTable/time-table.component";
import {TimeTableService} from "./timeTable/time-table.service";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    TimeTableComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
      TimeTableService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
