import {CalendarDate} from "./calendar-date";
export class Calendar {
    id: string;
    startDate: string;
    endDate: string;
    startDateObject: Date;
    endDateObject: Date;
    monday: boolean;
    tuesday: boolean;
    wednesday: boolean;
    thursday: boolean;
    friday: boolean;
    saturday: boolean;
    sunday: boolean;
    canBeDeleted: boolean;

    calendarDates: CalendarDate[];

}