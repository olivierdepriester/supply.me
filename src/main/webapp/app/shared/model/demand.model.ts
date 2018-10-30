import { IUser } from 'app/core/user/user.model';
import { IMaterial } from 'app/shared/model//material.model';
import { IProject } from 'app/shared/model//project.model';
import { Moment } from 'moment';
import { ISupplier } from './supplier.model';
import { IDemandCategory } from './demand-category.model';

export enum DemandStatus {
    NEW = 'NEW',
    WAITING_FOR_APPROVAL = 'WAITING_FOR_APPROVAL',
    APPROVED = 'APPROVED',
    REJECTED = 'REJECTED',
    ORDERED = 'ORDERED',
    PARTIALLY_DELIVERED = 'PARTIALLY_DELIVERED',
    FULLY_DELIVERED = 'FULLY_DELIVERED'
}

export interface IDemandStatusChange {
    id?: number;
    status?: DemandStatus;
    comment?: string;
    creationDate?: Moment;
    creationUser?: IUser;
}

export class DemandStatusChange implements IDemandStatusChange {
    constructor(
        public id?: number,
        public status?: DemandStatus,
        public comment?: string,
        public creationDate?: Moment,
        public creationUser?: IUser
    ) {}
}

export interface IDemand {
    id?: number;
    code?: string;
    quantity?: number;
    status?: DemandStatus;
    description?: string;
    expectedDate?: Moment;
    creationDate?: Moment;
    material?: IMaterial;
    project?: IProject;
    creationUser?: IUser;
    supplier?: ISupplier;
    demandCategory?: IDemandCategory;
    quantityOrdered?: number;
    quantityDelivered?: number;
    estimatedPrice?: number;
    validationAuthority?: any;
    reachedAuthority?: any;
    urgent?: boolean;
    planned?: boolean;
    useAnnualBudget?: boolean;
    whereUse?: string;
    unit?: string;
    vat?: number;
    demandStatusChanges?: IDemandStatusChange[];
}

export class Demand implements IDemand {
    constructor(
        public id?: number,
        public code?: string,
        public quantity?: number,
        public status?: DemandStatus,
        public description?: string,
        public expectedDate?: Moment,
        public creationDate?: Moment,
        public material?: IMaterial,
        public project?: IProject,
        public creationUser?: IUser,
        public supplier?: ISupplier,
        public demandCategory?: IDemandCategory,
        public quantityOrdered?: number,
        public quantityDelivered?: number,
        public estimatedPrice?: number,
        public validationAuthority?: any,
        public reachedAuthority?: any,
        public urgent?: boolean,
        public planned?: boolean,
        public useAnnualBudget?: boolean,
        public whereUse?: string,
        public unit?: string,
        public vat?: number,
        public demandStatusChanges?: IDemandStatusChange[]
    ) {}
}

export class DemandAllowance {
    constructor(
        public canEdit?: boolean,
        public canReject?: boolean,
        public canApprove?: boolean,
        public canDelete?: boolean,
        public canSendToApproval?: boolean
    ) {}
}

export class DemandListItem {
    constructor(public demand?: IDemand, public allowance?: DemandAllowance) {}
}

export class DemandSearchCriteria {
    /**
     * Initialize a criteria by cloning the parameter.
     * Method used to retrieve a criteria stored in session.
     *
     * @static
     * @param {DemandSearchCriteria} sessionCriteria Criteria retrieved from the session
     * @returns {DemandSearchCriteria} null if sessionCriteria is null. Otherwise a copy of sessionCriteria.
     * @memberof DemandSearchCriteria
     */
    public static of(sessionCriteria: DemandSearchCriteria): DemandSearchCriteria {
        if (sessionCriteria) {
            return new DemandSearchCriteria(
                sessionCriteria.query,
                sessionCriteria.status,
                sessionCriteria.material,
                sessionCriteria.project,
                sessionCriteria.creationUser
            );
        } else {
            return null;
        }
    }

    constructor(
        public query?: string,
        public status?: string[],
        public material?: IMaterial,
        public project?: IProject,
        public creationUser?: IUser
    ) {
        this.status = [];
    }

    /**
     * Get the search parameters as query object.
     *
     * @returns
     * @memberof DemandSearchCriteria
     */
    getQuery() {
        return {
            query: this.query,
            status: this.status,
            materialId: this.material != null ? this.material.id : null,
            projectId: this.project != null ? this.project.id : null,
            creationUserId: this.creationUser != null ? this.creationUser.id : null
        };
    }
    /**
     * Clear the search parameters.
     *
     * @memberof DemandSearchCriteria
     */
    clear() {
        this.query = '';
        this.status = [];
        this.material = null;
        this.project = null;
        this.creationUser = null;
    }

    isEmpty(): boolean {
        let result = true;
        Object.keys(this).forEach(key => {
            result = result && (this[key] == null || this[key] === '' || this[key].length === 0);
        });
        return result;
    }
}
