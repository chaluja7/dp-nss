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
  id: string;
  name: string;
  lat: number;
  lon: number;
  wheelChairCode: number;
  parentStopId: string;
  canBeDeleted: boolean;
}

export class Trip {
  headSign: string;
  wheelChair: string;
  route: Route;
}

export class Route {
  id: string;
  shortName: string;
  longName: string;
  typeCode: number;
  color: string;
  agencyId: string;
  agencyName: string;
  canBeDeleted: boolean;
}