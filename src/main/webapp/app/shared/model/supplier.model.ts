import { IMaterialAvailability } from 'app/shared/model//material-availability.model';
import { ISelectable } from 'app/shared/model/selectable.model';
import { IUser } from 'app/core';
import { Moment } from 'moment';

export interface ISupplier {
    id?: number;
    referenceNumber?: string;
    name?: string;
    rating?: number;
    temporary?: boolean;
    creationDate?: Moment;
    creationUser?: IUser;
    names?: IMaterialAvailability[];
}

export class Supplier implements ISupplier, ISelectable {
    constructor(
        public id?: number,
        public referenceNumber?: string,
        public name?: string,
        public rating?: number,
        public temporary?: boolean,
        public creationDate?: Moment,
        public creationUser?: IUser,
        public names?: IMaterialAvailability[]
    ) {}
}
