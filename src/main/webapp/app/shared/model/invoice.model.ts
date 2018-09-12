import { Moment } from 'moment';
import { ISupplier } from 'app/shared/model//supplier.model';
import { IUser } from 'app/core/user/user.model';

export const enum InvoiceStatus {
    NEW = 'NEW',
    PAID = 'PAID'
}

export interface IInvoice {
    id?: number;
    code?: string;
    status?: InvoiceStatus;
    sendingDate?: Moment;
    dueDate?: Moment;
    supplier?: ISupplier;
    creationUser?: IUser;
}

export class Invoice implements IInvoice {
    constructor(
        public id?: number,
        public code?: string,
        public status?: InvoiceStatus,
        public sendingDate?: Moment,
        public dueDate?: Moment,
        public supplier?: ISupplier,
        public creationUser?: IUser
    ) {}
}
