import { Moment } from 'moment';
import { IDemand } from 'app/shared/model//demand.model';

export interface IProject {
    id?: number;
    code?: string;
    description?: string;
    creationDate?: Moment;
    demands?: IDemand[];
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public creationDate?: Moment,
        public demands?: IDemand[]
    ) {}
}
