import { IMaterialAvailability } from 'app/shared/model//material-availability.model';
import { ISelectable } from 'app/shared/model/selectable.model';

export interface ISupplier {
    id?: number;
    referenceNumber?: string;
    name?: string;
    rating?: number;
    temporary?: boolean;
    names?: IMaterialAvailability[];
}

export class Supplier implements ISupplier, ISelectable {
    constructor(
        public id?: number,
        public referenceNumber?: string,
        public name?: string,
        public rating?: number,
        public temporary?: boolean,
        public names?: IMaterialAvailability[]
    ) {}
}
