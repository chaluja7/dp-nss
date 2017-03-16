export class StopTime {
    id: number;
    sequence: number;
    stopId: string;
    arrival: string;
    departure: string;
    arrivalObject: Date;
    departureObject: Date;

    constructor(stopTime?: StopTime) {
        if(stopTime) {
            this.id = stopTime.id;
            this.sequence = stopTime.sequence;
            this.stopId = stopTime.stopId;
            this.arrival = stopTime.arrival;
            this.departure = stopTime.departure;
            if(stopTime.arrivalObject) this.arrivalObject = new Date(stopTime.arrivalObject.getTime());
            if(stopTime.departureObject) this.departureObject = new Date(stopTime.departureObject.getTime());
        }
    }

}