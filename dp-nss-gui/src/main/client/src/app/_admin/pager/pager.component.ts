import {Component, Input} from "@angular/core";
import {Pager} from "../../_model/pager";

/**
 * Komponenta strankovani
 */
@Component({
    moduleId: module.id,
    selector: 'pager-component',
    templateUrl: './pager.component.html'
})
export class PagerComponent {

    @Input()
    component: any;

    @Input()
    pager: Pager;

    /**
     * nastavi stranku
     * @param page cislo stranky
     */
    setPage(page: number) {
        this.component.setPage(page);
    }

}
