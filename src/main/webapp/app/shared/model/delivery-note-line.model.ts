import { IDeliveryNote } from 'app/shared/model//delivery-note.model';
import { IPurchaseOrderLine } from 'app/shared/model//purchase-order-line.model';

export interface IDeliveryNoteLine {
    id?: number;
    lineNumber?: number;
    quantity?: number;
    deliveryNote?: IDeliveryNote;
    purchaseOrderLine?: IPurchaseOrderLine;
}

export class DeliveryNoteLine implements IDeliveryNoteLine {
    constructor(
        public id?: number,
        public lineNumber?: number,
        public quantity?: number,
        public deliveryNote?: IDeliveryNote,
        public purchaseOrderLine?: IPurchaseOrderLine
    ) {}
}
