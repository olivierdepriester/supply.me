import { Moment } from 'moment';
import { IMaterial } from 'app/shared/model//material.model';
import { ISupplier } from 'app/shared/model//supplier.model';

export interface IMaterialAvailability {
    id?: number;
    creationDate?: Moment;
    updateDate?: Moment;
    purchasePrice?: number;
    material?: IMaterial;
    supplier?: ISupplier;
}

export class MaterialAvailability implements IMaterialAvailability {
    constructor(
        public id?: number,
        public creationDate?: Moment,
        public updateDate?: Moment,
        public purchasePrice?: number,
        public material?: IMaterial,
        public supplier?: ISupplier
    ) {}
}
