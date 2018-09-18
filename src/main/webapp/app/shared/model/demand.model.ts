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
        public creationUser?: IUser
    ) {}
}

export class DemandSearchCriteria {
    constructor(public fullText?: string, public status?: DemandStatus, public materialId?: number, public projectId?: number) {}
}
