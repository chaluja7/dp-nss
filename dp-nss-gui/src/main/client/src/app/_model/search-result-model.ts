export class SearchResultModel {
  departureDate: string;
  arrivalDate: string;
  travelTime: number;
  stopTimes: StopTime[];
}

export class StopTime {
  stop: Stop;
  trip: Trip;
  arrival: string;
  departure: string;
}

export class Stop {
  name: string;
  lat: number;
  lon: number;
  wheelChair: string;
}

export class Trip {
  headSign: string;
  wheelChair: string;
  route: Route;
}

export class Route {
  shortName: string;
  longName: string;
  routeType: string;
  color: string;
}