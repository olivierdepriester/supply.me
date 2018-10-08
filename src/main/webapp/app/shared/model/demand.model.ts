import { Moment } from 'moment';
import { IMaterial } from 'app/shared/model//material.model';
import { IProject } from 'app/shared/model//project.model';
import { IUser } from 'app/core/user/user.model';

export const enum DemandStatus {
    NEW = 'NEW',
    WAITING_FOR_APPROVAL = 'WAITING_FOR_APPROVAL',
    APPROVED = 'APPROVED',
    REJECTED = 'REJECTED',
    ORDERED = 'ORDERED',
    PARTIALLY_DELIVERED = 'PARTIALLY_DELIVERED',
    FULLY_DELIVERED = 'FULLY_DELIVERED'
}

export interface IDemand {
    id?: number;
    quantity?: number;
    status?: DemandStatus;
    expectedDate?: Moment;
    creationDate?: Moment;
    material?: IMaterial;
    project?: IProject;
    creationUser?: IUser;
    quantityOrdered?: number;
    quantityDelivered?: number;
}

export class Demand implements IDemand {
    constructor(
        public id?: number,
        public quantity?: number,
        public status?: DemandStatus,
        public expectedDate?: Moment,
        public creationDate?: Moment,
        public material?: IMaterial,
        public project?: IProject,
        public creationUser?: IUser,
        public quantityOrdered?: number,
        public quantityDelivered?: number
    ) {}
}

export class DemandSearchCriteria {
    constructor(
        public query?: string,
        public status?: DemandStatus,
        public material?: IMaterial,
        public project?: IProject,
        public currentUser?: boolean
    ) {}

    getQuery() {
        return {
            query: this.query,
            status: this.status,
            materialId: this.material != null ? this.material.id : null,
            projectId: this.project != null ? this.project.id : null,
            currentUser: this.currentUser
        };
    }
}
