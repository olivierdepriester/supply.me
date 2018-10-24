import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService, Principal } from 'app/core';
import { Demand, IDemand, DemandStatus, DemandSearchCriteria } from 'app/shared/model/demand.model';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DemandDeletePopupComponent } from './demand-delete-dialog.component';
import { DemandDetailComponent } from './demand-detail.component';
import { DemandUpdateComponent } from './demand-update.component';
import { DemandComponent } from './demand.component';
import { DemandService } from './demand.service';
import { MaterialTemporaryPopupComponent } from './material-temporary-dialog.component';
import { DemandChangeStatusPopupComponent } from './demand-change-status-dialog.component';

@Injectable({ providedIn: 'root' })
export class DemandResolve implements Resolve<IDemand> {
    constructor(private service: DemandService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id: string = route.params['id'] ? route.params['id'] : null;
        if (id) {
            if (id.startsWith('PR')) {
                // TODO : Implements find by PR code
            } else {
                return this.service.find(parseInt(id, 10)).pipe(map((demand: HttpResponse<Demand>) => demand.body));
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
            demand: DemandWithChangesResolve
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
            demand: DemandResolve
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
            demand: DemandResolve
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
                'ROLE_VALIDATION_LVL1',
                'ROLE_VALIDATION_LVL2',
                'ROLE_VALIDATION_LVL3',
                'ROLE_VALIDATION_LVL4',
                'ROLE_VALIDATION_LVL5'
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
            demand: DemandResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demand.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
