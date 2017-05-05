import {Pipe, PipeTransform} from "@angular/core";
import {AppSettings} from "../_common/app.settings";

/**
 * Formatter typu spoje
 */
@Pipe({name: 'routeType'})
export class RouteTypePipe implements PipeTransform {

    transform(code: number): string {
        return AppSettings.getRouteTypeText(code);
    }

}