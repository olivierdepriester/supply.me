import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserRouteAccessService, Principal } from 'app/core';
import { Demand, IDemand, DemandStatus, DemandSearchCriteria } from 'app/shared/model/demand.model';
import { IAttachmentFile } from 'app/shared/model/attachment-file.model';
import { DemandDeletePopupComponent } from './demand-delete-dialog.component';
import { DemandDetailComponent } from './demand-detail.component';
import { DemandUpdateComponent } from './demand-update.component';
import { DemandComponent } from './demand.component';
import { DemandService } from './demand.service';
import { MaterialTemporaryPopupComponent } from './material-temporary-dialog.component';
import { DemandChangeStatusPopupComponent } from './demand-change-status-dialog.component';
import { SupplierTemporaryPopupComponent } from './supplier-temporary-dialog.component';
import { SupplierResolve } from '../supplier';
import { MaterialResolve } from '../material';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { PurchaseOrderLineService } from '../purchase-order-line';

@Injectable({ providedIn: 'root' })
export class DemandResolve implements Resolve<IDemand> {
    constructor(private service: DemandService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id: string = route.params['id'] ? route.params['id'] : null;
        if (id) {
            if (id.startsWith('PR')) {
                // TODO : Implements find by PR code
            } else {
                return this.service.find(parseInt(id, 10)).pipe(map((response: HttpResponse<Demand>) => response.body));
            }
        } else {
            const originId: number = route.queryParams['origin'] ? route.queryParams['origin'] : null;
            if (originId) {
                return this.service.find(originId).pipe(
                    map((response: HttpResponse<Demand>) => {
                        const newDemand = response.body;
                        newDemand.id = null;
                        newDemand.code = null;
                        newDemand.status = DemandStatus.NEW;
                        return newDemand;
                    })
                );
            }
        }
        return of(new Demand());
    }
}
@Injectable({ providedIn: 'root' })
export class DemandWithChangesResolve implements Resolve<IDemand> {
    constructor(private service: DemandService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id, true).pipe(map((demand: HttpResponse<Demand>) => demand.body));
        }
        return of(new Demand());
    }
}

@Injectable({ providedIn: 'root' })
export class AttachmentFilesResolve implements Resolve<IAttachmentFile[]> {
    constructor(private service: DemandService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.getAttachmentFiles(id).pipe(map((res: HttpResponse<IAttachmentFile[]>) => res.body));
        }
        return [];
    }
}

@Injectable({ providedIn: 'root' })
export class PurchaserOrderLinesResolve implements Resolve<IPurchaseOrderLine[]> {
    constructor(private service: PurchaseOrderLineService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.getBydemandId(id).pipe(map((res: HttpResponse<IPurchaseOrderLine[]>) => res.body));
        }
        return [];
    }
}
@Injectable({ providedIn: 'root' })
export class StatusResolve implements Resolve<DemandStatus> {
    constructor() {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const status = route.params['status'] ? route.params['status'] : null;
        return status as DemandStatus;
    }
}
@Injectable({ providedIn: 'root' })
export class MySearchCriteriaResolve implements Resolve<DemandSearchCriteria> {
    constructor(private principal: Principal) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const criteria = new DemandSearchCriteria();
        criteria.status.push(
            DemandStatus.NEW,
            DemandStatus.WAITING_FOR_APPROVAL,
            DemandStatus.REJECTED,
            DemandStatus.APPROVED,
            DemandStatus.ORDERED,
            DemandStatus.PARTIALLY_DELIVERED
        );
        this.principal.identity().then(account => {
            criteria.creationUser = account;
        });
        return criteria;
    }
}
@Injectable({ providedIn: 'root' })
export class WaitingForApprovalSearchCriteriaResolve implements Resolve<DemandSearchCriteria> {
    constructor() {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const criteria = new DemandSearchCriteria();
        criteria.status.push(DemandStatus.WAITING_FOR_APPROVAL);
        return criteria;
    }
}

export const demandRoute: Routes = [
    {
        path: 'demand',
        component: DemandComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand/my',
        component: DemandComponent,
        resolve: {
            criteria: MySearchCriteriaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand/waitingapproval',
        component: DemandComponent,
        resolve: {
            criteria: WaitingForApprovalSearchCriteriaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand/:id/view',
        component: DemandDetailComponent,
        resolve: {
            demand: DemandWithChangesResolve,
            attachments: AttachmentFilesResolve,
            purchaseOrderLines: PurchaserOrderLinesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand/new',
        component: DemandUpdateComponent,
        resolve: {
            demand: DemandResolve,
            attachments: AttachmentFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand/:id/edit',
        component: DemandUpdateComponent,
        resolve: {
            demand: DemandResolve,
            attachments: AttachmentFilesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const demandPopupRoute: Routes = [
    {
        path: 'demand/:id/delete',
        component: DemandDeletePopupComponent,
        resolve: {
            demand: DemandResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'demand/:id/:status/comment',
        component: DemandChangeStatusPopupComponent,
        resolve: {
            demand: DemandResolve,
            status: StatusResolve
        },
        data: {
            authorities: [
                'ROLE_VALIDATOR_LVL1',
                'ROLE_VALIDATOR_LVL2',
                'ROLE_VALIDATOR_LVL3',
                'ROLE_VALIDATOR_LVL4',
                'ROLE_VALIDATOR_LVL5'
            ],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'demand/material/new',
        component: MaterialTemporaryPopupComponent,
        resolve: {
            material: MaterialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'demand/supplier/new',
        component: SupplierTemporaryPopupComponent,
        resolve: {
            supplier: SupplierResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
