import {Pipe, PipeTransform} from "@angular/core";
import {AppSettings} from "../_common/app.settings";

@Pipe({name: 'calendarExceptionType'})
export class CalendarExceptionTypePipe implements PipeTransform {

    transform(code: number): string {
        return AppSettings.getExceptionTypeText(code);
    }

}