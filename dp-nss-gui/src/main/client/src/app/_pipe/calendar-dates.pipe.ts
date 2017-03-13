import {Pipe, PipeTransform} from "@angular/core";
import {Calendar} from "../_model/calendar";

@Pipe({name: 'calendarDates'})
export class CalendarDatesPipe implements PipeTransform {

    transform(calendar: Calendar): string {
        if(!calendar) return '';

        let s = '';
        if(calendar.monday) s += 'PO | ';
        if(calendar.tuesday) s += 'ÚT | ';
        if(calendar.wednesday) s += 'ST | ';
        if(calendar.thursday) s += 'ČT | ';
        if(calendar.friday) s += 'PÁ | ';
        if(calendar.saturday) s += 'SO | ';
        if(calendar.sunday) s += 'NE | ';

        if(s.endsWith(' | ')) s = s.substring(0, s.length - 3);
        return s;
    }

}