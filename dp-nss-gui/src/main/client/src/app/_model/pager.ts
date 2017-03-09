export class Pager {
    totalItems: number;
    currentPage: number;
    pageSize: number;
    totalPages: number;
    startPage: number;
    endPage: number;
    startIndex: number;
    endIndex: number;
    pages: number[];

    constructor(totalItems?: number, currentPage?: number, pageSize?: number, totalPages?: number,
                startPage?: number, endPage?: number, startIndex?: number, endIndex?: number, pages?: number[]) {

        this.totalItems = totalItems;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.startPage = startPage;
        this.endPage = endPage;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.pages = pages;
    }
}