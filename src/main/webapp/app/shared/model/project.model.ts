import { Moment } from 'moment';
import { IDemand } from 'app/shared/model//demand.model';
import { IUser } from 'app/core';
import { ISelectable } from './selectable.model';

export interface IProject {
    id?: number;
    code?: string;
    description?: string;
    creationDate?: Moment;
    creationUser?: IUser;
    headUser?: IUser;
    demands?: IDemand[];
}

export class Project implements IProject, ISelectable {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public creationDate?: Moment,
        public creationUser?: IUser,
        public headUser?: IUser,
        public demands?: IDemand[]
    ) {}
}
