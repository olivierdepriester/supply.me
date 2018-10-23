import { Moment } from 'moment';
import { IMaterialAvailability } from 'app/shared/model//material-availability.model';
import { IDemand } from 'app/shared/model//demand.model';
import { IUser } from 'app/core';
import { IMaterialCategory } from './material-category.model';

export interface IMaterial {
    id?: number;
    partNumber?: string;
    name?: string;
    description?: string;
    creationDate?: Moment;
    creationUser?: IUser;
    temporary?: boolean;
    codes?: IMaterialAvailability[];
    demands?: IDemand[];
    materialCategory?: IMaterialCategory;
    estimatedPrice?: number;
}

export class Material implements IMaterial {
    constructor(
        public id?: number,
        public partNumber?: string,
        public name?: string,
        public description?: string,
        public creationDate?: Moment,
        public creationUser?: IUser,
        public temporary?: boolean,
        public codes?: IMaterialAvailability[],
        public demands?: IDemand[],
        public materialCategory?: IMaterialCategory,
        public estimatedPrice?: number
    ) {}
}
