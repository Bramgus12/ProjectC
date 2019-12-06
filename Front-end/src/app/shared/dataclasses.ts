export interface Address {
  id?: number;
  street: string;
  number: number;
  city: string;
  postal: string;
}

export interface Building {
  id?: number,
  address_id: number,
  name: string
}

export interface NavigationState {
  from: string,
  to: string,
  departNow: boolean,
  timeMode: TimeMode,
  time: string,
  fromNode: number,
  toNode: number
}

export enum TimeMode {
  ARRIVAL_BY,
  DEPART_BY
}

export interface TimeModeOption {
  name: string,
  value: TimeMode
}

export class GeoLocation {
  latitute: number;
  longtitute: number;

  constructor(lat: number, long: number){
    this.latitute = lat;
    this.longtitute = long;
  }

  toString(): string{
    return this.latitute + ',' + this.longtitute;
  }
}

export interface Node {
  number: number;
  type: string;
  code: string;
  label: string;
  x: number;
  y: number;
  z: number;
}

export interface NodeConnection {
  node1: Node;
  node2: Node;
  node1Vector: THREE.Vector3;
  node2Vector: THREE.Vector3;
  node1Distance: number;
  node2Distance: number;
  distance: number;
}
