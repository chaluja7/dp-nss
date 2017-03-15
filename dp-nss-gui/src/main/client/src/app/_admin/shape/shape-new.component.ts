import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {AppSettings} from "../../_common/app.settings";
import {AbstractShapeComponent} from "./abstract-shape.component";
import {Shapes} from "../../_model/shapes";
@Component({
  moduleId: module.id,
  selector: 'shape-new-component',
  templateUrl: './shape.component.html'
})
export class ShapeNewComponent extends AbstractShapeComponent implements OnInit {

  newRecord = true;

  ngOnInit(): void {
    this.shape = new Shapes();
    this.shape.shapes = [];
  }

  onSubmit(): void {
    this.loading = true;

    this.adminShapeService.create(this.shape)
        .subscribe(route => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR;
              this.loading = false;
            });
  }

}
