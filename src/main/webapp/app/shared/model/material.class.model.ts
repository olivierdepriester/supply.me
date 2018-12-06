import { Moment } from 'moment';
import { IMaterialAvailability } from './material-availability.model';
import { IDemand } from './demand.model';
import { IUser } from '../../core';
import { IMaterialCategory } from './material-category.model';
import { IMaterial } from './material.model';

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
