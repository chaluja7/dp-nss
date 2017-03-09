import {Injectable} from "@angular/core";
import {AppSettings} from "../../_common/app.settings";
import {Pager} from "../../_model/pager";

@Injectable()
export class PagerService {

  getPager(totalItems: number, currentPage: number = 1, pageSize: number = AppSettings.DEFAULT_PAGE_LIMIT): Pager {
    // calculate total pages
    let totalPages = Math.ceil(totalItems / pageSize);

    let startPage: number, endPage: number;
    if (totalPages <= 10) {
      // less than 10 total pages so show all
      startPage = 1;
      endPage = totalPages;
    } else {
      // more than 10 total pages so calculate start and end pages
      if (currentPage <= 6) {
        startPage = 1;
        endPage = 10;
      } else if (currentPage + 4 >= totalPages) {
        startPage = totalPages - 9;
        endPage = totalPages;
      } else {
        startPage = currentPage - 5;
        endPage = currentPage + 4;
      }
    }

    // calculate start and end item indexes
    let startIndex = (currentPage - 1) * pageSize;
    let endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

    // create an array of pages to ng-repeat in the pager control
    // let pages = _.range(startPage, endPage + 1);
    let pages = [];
    for(let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    return new Pager(totalItems, currentPage, pageSize, totalPages, startPage, endPage, startIndex, endIndex, pages);
  }

  public static getSortHeaderValue(column: string, orderAsc: boolean) {
    return column + ':' + (orderAsc ? 'asc' : 'desc');
  }
}