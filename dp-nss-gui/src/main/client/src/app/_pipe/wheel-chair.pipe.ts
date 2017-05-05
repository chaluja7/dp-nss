import {Pipe, PipeTransform} from "@angular/core";
import {AppSettings} from "../_common/app.settings";

/**
 * Formatter typu bezbarierovosti
 */
@Pipe({name: 'wheelChair'})
export class WheelChairPipe implements PipeTransform {

    transform(code: number): string {
        return AppSettings.getWheelChairText(code);
    }

}