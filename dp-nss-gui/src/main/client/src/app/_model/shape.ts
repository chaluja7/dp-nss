export class Shape {
    sequence: number;
    lat: number;
    lon: number;

    constructor(shape?: Shape) {
        if(shape) {
            this.sequence = shape.sequence;
            this.lat = shape.lat;
            this.lon = shape.lon;
        }
    }
}
