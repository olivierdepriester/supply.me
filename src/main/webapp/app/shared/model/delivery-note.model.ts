import { Moment } from 'moment';
import { ISupplier } from 'app/shared/model//supplier.model';
import { IUser } from 'app/core/user/user.model';

export const enum DeliveryNoteStatus {
    NEW = 'NEW'
}

export interface IDeliveryNote {
    id?: number;
    code?: string;
    deliveryDate?: Moment;
    status?: DeliveryNoteStatus;
    creationDate?: Moment;
    supplier?: ISupplier;
    creationUser?: IUser;
}

export class DeliveryNote implements IDeliveryNote {
    constructor(
        public id?: number,
        public code?: string,
        public deliveryDate?: Moment,
        public status?: DeliveryNoteStatus,
        public creationDate?: Moment,
        public supplier?: ISupplier,
        public creationUser?: IUser
    ) {}
}
