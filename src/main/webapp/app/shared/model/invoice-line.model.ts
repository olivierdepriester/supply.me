import { IPurchaseOrder } from 'app/shared/model//purchase-order.model';
import { IMaterial } from 'app/shared/model//material.model';

export interface IInvoiceLine {
    id?: number;
    lineNumber?: number;
    quantity?: number;
    amountNet?: number;
    amountWithTax?: number;
    purchaseOrder?: IPurchaseOrder;
    material?: IMaterial;
}

export class InvoiceLine implements IInvoiceLine {
    constructor(
        public id?: number,
        public lineNumber?: number,
        public quantity?: number,
        public amountNet?: number,
        public amountWithTax?: number,
        public purchaseOrder?: IPurchaseOrder,
        public material?: IMaterial
    ) {}
}
