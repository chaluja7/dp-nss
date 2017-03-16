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

  public timepickerArrivalOpts = {
    showMeridian: false,
    placeholder: 'Příjezd',
    language: 'cs',
    showSeconds: true
  };

  public timepickerDepartureOpts = {
    showMeridian: false,
    placeholder: 'Odjezd',
    language: 'cs',
    showSeconds: true
  };

  public static getFormattedDateOnly(date: Date) : string {
    return date ? date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() : null;
  }

  public static getFormattedTimeOnly(date: Date) : string {
    return date ? date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds() : null;
  }

  public static getFormattedDate(date: Date, time: Date) : string {
    return date && time ? date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() + ' ' + time.getHours() + ':' + time.getMinutes() : null;
  }

  public static getDateObjectFromString(s: string): Date {
    if(!s) return null;
    let date = new Date();

    date.setDate(+DateService.getDayOfMonth(s));
    date.setMonth(+DateService.getMonthOfYear(s) - 1);
    date.setFullYear(+DateService.getYear(s));
    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);
    date.setMilliseconds(0);
    return date;
  }

  public static getDayOfMonth(s: string): string {
    return s.split('.')[0];
  }

  public static getMonthOfYear(s: string): string {
    return s.split('.')[1];
  }

  public static getYear(s: string): string {
    return s.split('.')[2];
  }

  public static getTimeObjectFromString(s: string): Date {
    if(!s) return null;
    let date = new Date();

    date.setDate(0);
    date.setMonth(0);
    date.setFullYear(0);
    date.setHours(+DateService.getHours(s));
    date.setMinutes(+DateService.getMinutes(s));
    date.setSeconds(+DateService.getSeconds(s));
    date.setMilliseconds(0);
    return date;
  }

  public static getHours(s: string): string {
    return s.split(':')[0];
  }

  public static getMinutes(s: string): string {
    return s.split(':')[1];
  }

  public static getSeconds(s: string): string {
    let split: string[] = s.split(':');
    if(split.length > 2) {
      return s.split(':')[2];
    }

    return '00';
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