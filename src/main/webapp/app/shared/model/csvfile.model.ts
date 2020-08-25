import { Moment } from 'moment';

export interface ICsvfile {
  id?: number;
  name?: string;
  data?: any;
  date?: Moment;
}

export class Csvfile implements ICsvfile {
  constructor(public id?: number, public name?: string, public data?: any, public date?: Moment) {}
}
