import {Component, Input} from "@angular/core";

@Component({
  moduleId: module.id,
  selector: 'route-img',
  templateUrl: './route-img.component.html'
})
export class RouteImgComponent {

    @Input()
    routeType: number;

    getImgSrc() {
        let path = "/assets/img/";
        switch(this.routeType) {
            case 0:
                path += "tram_p";
                break;
            case 1:
                path += "metro_p";
                break;
            case 2:
                path += "train_p";
                break;
            case 3:
                path += "bus_p";
                break;
            default:
                return '';
        }

        return path + '.gif';
    }

}
