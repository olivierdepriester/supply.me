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
