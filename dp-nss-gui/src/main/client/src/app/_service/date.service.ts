import {Injectable} from "@angular/core";

@Injectable()
export class DateService {

  public datepickerOpts = {
    autoclose: true,
    todayBtn: 'linked',
    todayHighlight: true,
    format: 'dd.mm.yyyy (DD)',
    placeholder: 'Datum',
    language: 'cs',
    icon: 'fa fa-calendar'
  };

  public timepickerOpts = {
    showMeridian: false,
    placeholder: 'Čas',
    language: 'cs',
    defaultTime: 'current'
  };

  public static getFormattedDateOnly(date: Date) : string {
    return date ? date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() : null;
  }

  public static getFormattedDate(date: Date, time: Date) : string {
    return date && time ? date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() + ' ' + time.getHours() + ':' + time.getMinutes() : null;
  }

  public static getDateObjectFromString(s: string): Date {
    if(!s) return null;
    let date = new Date();
    date.setFullYear(+DateService.getDayOfMonth(s), +DateService.getMonthOfYear(s), +DateService.getYear(s));
    console.log(date);
    return date;
  }

  public static getDayOfMonth(s: string) {
    return s.split('.')[0];
  }

  public static getMonthOfYear(s: string) {
    return s.split('.')[1];
  }

  public static getYear(s: string) {
    return s.split('.')[2];
  }

  public getFormattedTimePeriod(numOfSeconds: number) : string {
    if(!numOfSeconds) return null;

    let minutes = Math.floor(numOfSeconds / 60);
    if(minutes < 1) return numOfSeconds + ' vteřin';

    let hours = Math.floor(minutes / 60);
    if(hours < 1) return minutes + ' minut';

    return hours + ' hodin ' + minutes % 60 + ' minut';
  }

}