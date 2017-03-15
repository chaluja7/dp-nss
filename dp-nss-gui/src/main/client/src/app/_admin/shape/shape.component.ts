import "rxjs/add/operator/switchMap";
import {Component, OnInit} from "@angular/core";
import {Params} from "@angular/router";
import {AppSettings} from "../../_common/app.settings";
import {AbstractShapeComponent} from "./abstract-shape.component";
@Component({
  moduleId: module.id,
  selector: 'shape-component',
  templateUrl: './shape.component.html'
})
export class ShapeComponent extends AbstractShapeComponent implements OnInit {

  newRecord = false;

  ngOnInit(): void {
    this.route.params
        .switchMap((params: Params) => this.adminShapeService.getShape(params['id']))
        .subscribe(shape => {
            this.shape = shape;
        }, err  => {});
  }

  onSubmit(): void {
    this.loading = true;

    if(!this.checkShapes()) {
        return;
    }

    this.adminShapeService.update(this.shape)
        .subscribe(shape => {
            this.userService.setMsg(AppSettings.SAVE_SUCCESS);
            this.goBack()
        },
            err  => {
              this.error = AppSettings.SAVE_ERROR;
              this.loading = false;
            });
  }

  doDelete(): void {
      this.adminShapeService.delete(this.shape.id)
          .subscribe(() => {
                  this.userService.setMsg(AppSettings.DELETE_SUCCESS);
                  this.goBack()
              },
            err => {
                this.error = AppSettings.SAVE_ERROR;
                this.loading = false;
            });

  }

}
