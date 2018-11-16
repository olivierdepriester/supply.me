import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IDemandCategory {
    id?: number;
    name?: string;
    description?: string;
    creationDate?: Moment;
    creationUser?: IUser;
}

export class DemandCategory implements IDemandCategory {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public creationDate?: Moment,
        public creationUser?: IUser
    ) {}
}
