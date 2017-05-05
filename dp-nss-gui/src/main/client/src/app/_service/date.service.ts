import {Injectable} from "@angular/core";

/**
 * Helper pro date a time picker
 */
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

    /**
     * @param date dateTime object
     * @returns {string} czech date string
     */
    public static getFormattedDateOnly(date: Date): string {
        return date ? date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() : null;
    }

    /**
     * @param date dateTime object
     * @returns {string} czech time string
     */
    public static getFormattedTimeOnly(date: Date): string {
        return date ? date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds() : null;
    }

    /**
     * @param date date object
     * @param time time object
     * @returns {string} czech date and time string
     */
    public static getFormattedDate(date: Date, time: Date): string {
        return date && time ? date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() + ' ' + time.getHours() + ':' + time.getMinutes() : null;
    }

    /**
     * @param s czech date string
     * @returns {Date} date object
     */
    public static getDateObjectFromString(s: string): Date {
        if (!s) return null;
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

    /**
     * @param s czech date string
     * @returns {string} den v mesici
     */
    public static getDayOfMonth(s: string): string {
        return s.split('.')[0];
    }

    /**
     * @param s czech date string
     * @returns {string} mesic v roce
     */
    public static getMonthOfYear(s: string): string {
        return s.split('.')[1];
    }

    /**
     * @param s czech date string
     * @returns {string} rok
     */
    public static getYear(s: string): string {
        return s.split('.')[2];
    }

    /**
     * @param s czech date time string
     * @returns {Date} date object from string
     */
    public static getTimeObjectFromString(s: string): Date {
        if (!s) return null;
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

    /**
     * @param s czech time string
     * @returns {string} pocet hodin dne
     */
    public static getHours(s: string): string {
        return s.split(':')[0];
    }

    /**
     * @param s czech time string
     * @returns {string} pocet minut hodiny
     */
    public static getMinutes(s: string): string {
        return s.split(':')[1];
    }

    /**
     * @param s czech time string
     * @returns {string} pocet vterin minuty
     */
    public static getSeconds(s: string): string {
        let split: string[] = s.split(':');
        if (split.length > 2) {
            return s.split(':')[2];
        }

        return '00';
    }

    /**
     * @param numOfSeconds pocet vterin
     * @returns {string} casovy udaj dle poctu vterin
     */
    public getFormattedTimePeriod(numOfSeconds: number): string {
        if (!numOfSeconds) return null;

        let minutes = Math.floor(numOfSeconds / 60);
        if (minutes < 1) return numOfSeconds + this.getSecondsLabel(numOfSeconds);

        let hours = Math.floor(minutes / 60);
        if (hours < 1) return minutes + this.getMinutesLabel(minutes);

        return hours + this.getHoursLabel(hours) + minutes % 60 + this.getMinutesLabel(minutes % 60);
    }

    /**
     * @param numOfSeconds pocet vterin
     * @returns {string} label nasklonovany dle poctu vterin
     */
    private getSecondsLabel(numOfSeconds: number): string {
        if (numOfSeconds == 1) return ' vteřina';
        if (numOfSeconds >= 2 && numOfSeconds <= 4) return ' vteřiny';
        return ' vteřin';
    }

    /**
     * @param numOfMinutes pocet minut
     * @returns {string} label nasklonovany dle poctu minut
     */
    private getMinutesLabel(numOfMinutes: number): string {
        if (numOfMinutes == 1) return ' minuta';
        if (numOfMinutes >= 2 && numOfMinutes <= 4) return ' minuty';
        return ' minut';
    }

    /**
     * @param numOfHours pocet hodin
     * @returns {any} label nasklonovany dle poctu hodin
     */
    private getHoursLabel(numOfHours: number): string {
        if (numOfHours == 1) return ' hodina ';
        if (numOfHours >= 2 && numOfHours <= 4) return ' hodiny ';
        return ' hodin ';
    }

}