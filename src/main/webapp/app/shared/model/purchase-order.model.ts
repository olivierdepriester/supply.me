import { Moment } from 'moment';
import { ISupplier } from 'app/shared/model//supplier.model';
import { IUser } from 'app/core/user/user.model';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

export const enum PurchaseOrderStatus {
    NEW = 'NEW',
    SENT = 'SENT',
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
    quantity?: number;
    amount?: number;
    numberOfMaterials?: number;
    purchaseOrderLines?: IPurchaseOrderLine[];
}

export class PurchaseOrder implements IPurchaseOrder {
    constructor(
        public id?: number,
        public code?: string,
        public expectedDate?: Moment,
        public status?: PurchaseOrderStatus,
        public creationDate?: Moment,
        public supplier?: ISupplier,
        public creationUser?: IUser,
        public purchaseOrderLines?: IPurchaseOrderLine[],
        public quantity?: number,
        public amount?: number,
        public numberOfMaterials?: number
    ) {}
}
