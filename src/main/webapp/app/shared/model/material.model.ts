import { Moment } from 'moment';
import { IMaterialAvailability } from 'app/shared/model//material-availability.model';
import { IDemand } from 'app/shared/model//demand.model';

export interface IMaterial {
    id?: number;
    partNumber?: string;
    name?: string;
    description?: string;
    creationDate?: Moment;
    codes?: IMaterialAvailability[];
    demands?: IDemand[];
}

export class Material implements IMaterial {
    constructor(
        public id?: number,
        public partNumber?: string,
        public name?: string,
        public description?: string,
        public creationDate?: Moment,
        public codes?: IMaterialAvailability[],
        public demands?: IDemand[]
    ) {}
}
