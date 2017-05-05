import "rxjs/add/operator/switchMap";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {UserService} from "../../_service/user.service";
import {Shapes} from "../../_model/shapes";
import {AdminShapeService} from "../../_service/_admin/admin-shape.service";
import {Shape} from "../../_model/shape";

/**
 * Komponenta administrace prujezdnich bodu
 */
@Component({
    moduleId: module.id
})
export abstract class AbstractShapeComponent {

    shape: Shapes;
    loading = false;
    error = '';
    abstract newRecord: boolean;

    constructor(protected adminShapeService: AdminShapeService, protected route: ActivatedRoute, protected location: Location,
                protected userService: UserService) {
    }

    /**
     * vynuceni onInit na potomku
     */
    abstract ngOnInit(): void;

    /**
     * obsluha odeslani formulare
     */
    abstract onSubmit(): void;

    /**
     * zkontroluje unikatnost sequence v listu
     * @returns {boolean} true, pokud je shape unikatni, false jinak
     */
    checkShapes(): boolean {
        if (this.shape.shapes && this.shape.shapes.length > 0) {
            let set: Set<number> = new Set<number>();

            for (let c of this.shape.shapes) {
                if (set.has(c.sequence)) {
                    this.error = 'Pořadí musí být unikátní napříč seznamem.';
                    this.loading = false;
                    return false;
                }

                set.add(c.sequence);
            }
        }

        return true;
    }

    /**
     * prida novy bod na index pole
     * @param index index pole
     */
    addNewShape(index: number): void {
        if (!this.shape.shapes) this.shape.shapes = [];

        //neni mozne pouzit jednoduchy splice, protoze je treba deep copy
        let arr: Shape[] = [];
        for (let i = 0; i < index; i++) {
            arr[i] = new Shape(this.shape.shapes[i]);
            arr[i].sequence = i + 1;
        }


        arr[index] = new Shape();
        arr[index].sequence = index + 1;

        for (let i = index; i < this.shape.shapes.length; i++) {
            arr[i + 1] = new Shape(this.shape.shapes[i]);
            arr[i + 1].sequence = i + 2;
        }

        this.shape.shapes = arr;
    }

    /**
     * odstrani bod z indexu pole
     * @param index index pole
     */
    deleteShape(index: number) {
        this.shape.shapes.splice(index, 1);

        //a od indexu snizim vsechny sequence o 1
        for (let i = index; i < this.shape.shapes.length; i++) {
            this.shape.shapes[i].sequence = this.shape.shapes[i].sequence - 1;
        }
    }

    confirm(msg: string): boolean {
        return confirm(msg);
    }

    /**
     * vrati se zpet v historii
     */
    goBack(): void {
        this.location.back();
    }

}
