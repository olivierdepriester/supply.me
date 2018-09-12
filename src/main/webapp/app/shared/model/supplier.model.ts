import { IMaterialAvailability } from 'app/shared/model//material-availability.model';

export interface ISupplier {
    id?: number;
    referenceNumber?: string;
    name?: string;
    names?: IMaterialAvailability[];
}

export class Supplier implements ISupplier {
    constructor(public id?: number, public referenceNumber?: string, public name?: string, public names?: IMaterialAvailability[]) {}
}
