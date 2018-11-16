import { IDeliveryNote } from 'app/shared/model//delivery-note.model';
import { IPurchaseOrderLine } from 'app/shared/model//purchase-order-line.model';
import { IMaterial } from 'app/shared/model//material.model';

export interface IDeliveryNoteLine {
    id?: number;
    lineNumber?: number;
    quantity?: number;
    deliveryNote?: IDeliveryNote;
    purchaseOrderLine?: IPurchaseOrderLine;
    material?: IMaterial;
}

export class DeliveryNoteLine implements IDeliveryNoteLine {
    constructor(
        public id?: number,
        public lineNumber?: number,
        public quantity?: number,
        public deliveryNote?: IDeliveryNote,
        public purchaseOrderLine?: IPurchaseOrderLine,
        public material?: IMaterial
    ) {}
}
