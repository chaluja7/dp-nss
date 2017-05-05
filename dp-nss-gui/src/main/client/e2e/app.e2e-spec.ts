import {DpNssClientPage} from "./app.po";

describe('dp-nss-client App', function () {
    let page: DpNssClientPage;

    beforeEach(() => {
        page = new DpNssClientPage();
    });

    it('should display message saying app works', () => {
        page.navigateTo();
        expect(page.getParagraphText()).toEqual('app works!');
    });
});
