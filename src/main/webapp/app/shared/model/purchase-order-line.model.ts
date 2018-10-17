import { IPurchaseOrder } from 'app/shared/model//purchase-order.model';
import { IDemand } from 'app/shared/model//demand.model';

export interface IPurchaseOrderLine {
    id?: number;
    lineNumber?: number;
    quantity?: number;
    quantityDelivered?: number;
    orderPrice?: number;
    purchaseOrder?: IPurchaseOrder;
    demand?: IDemand;
}

export class PurchaseOrderLine implements IPurchaseOrderLine {
    constructor(
        public id?: number,
        public lineNumber?: number,
        public quantity?: number,
        public quantityDelivered?: number,
        public orderPrice?: number,
        public purchaseOrder?: IPurchaseOrder,
        public demand?: IDemand
    ) {}
}
