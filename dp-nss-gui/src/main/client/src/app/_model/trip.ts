import {StopTime} from "./stopTime";
export class Trip {
    id: string;
    headSign: string;
    shapeId: string;
    calendarId: string;
    wheelChairCode: number;
    routeId: string;
    routeShortName: string;
    canBeDeleted: boolean;

    stopTimes: StopTime[];
}