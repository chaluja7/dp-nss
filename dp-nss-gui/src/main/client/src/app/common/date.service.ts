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
    language: 'cs'
  };

  public static getFormattedDate(date: Date) : string {
    return date ? date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() + ' ' + date.getHours() + ':' + date.getMinutes() : null;
  }

}