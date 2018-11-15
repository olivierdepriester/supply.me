import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export enum DemandCategoryKey {
    OTHER_EXPENSES = 'OTHER_EXPENSES'
}

export interface IDemandCategory {
    id?: number;
    name?: string;
    description?: string;
    key?: DemandCategoryKey;
    creationDate?: Moment;
    creationUser?: IUser;
}

export class DemandCategory implements IDemandCategory {
    constructor(
        public id?: number,
        public name?: string,
        public key?: DemandCategoryKey,
        public description?: string,
        public creationDate?: Moment,
        public creationUser?: IUser
    ) {}
}
