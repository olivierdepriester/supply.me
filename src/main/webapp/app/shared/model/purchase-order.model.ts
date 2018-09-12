import { Moment } from 'moment';
import { ISupplier } from 'app/shared/model//supplier.model';
import { IUser } from 'app/core/user/user.model';

export const enum PurchaseOrderStatus {
    NEW = 'NEW',
    PARTIALLY_DELIVERED = 'PARTIALLY_DELIVERED',
    BILLED = 'BILLED'
}

export interface IPurchaseOrder {
    id?: number;
    code?: string;
    expectedDate?: Moment;
    status?: PurchaseOrderStatus;
    creationDate?: Moment;
    supplier?: ISupplier;
    creationUser?: IUser;
}

export class PurchaseOrder implements IPurchaseOrder {
    constructor(
        public id?: number,
        public code?: string,
        public expectedDate?: Moment,
        public status?: PurchaseOrderStatus,
        public creationDate?: Moment,
        public supplier?: ISupplier,
        public creationUser?: IUser
    ) {}
}
