import {Component, OnInit} from "@angular/core";
import {Observable, Subject} from "rxjs";
import {StopService} from "../_service/stop.service";

@Component({
  moduleId: module.id,
  selector: 'stop-search-component',
  templateUrl: './stop-search.component.html',
  styleUrls: [ './stop-search.component.css' ]
})
export class StopSearchComponent implements OnInit {

  stops: Observable<string[]>;
  private searchTerms = new Subject<string>();

  constructor(private stopService: StopService) { }

  ngOnInit(): void {

    this.stops = this.searchTerms
        .debounceTime(300)
        .distinctUntilChanged()
        .switchMap((term:string) => term.length > 2 ? this.stopService.search(term, 'pid') : Observable.of<string[]>([]))
        .catch(error => {
          console.log(error);
          return Observable.of<string[]>([]);
        });
  }

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

}