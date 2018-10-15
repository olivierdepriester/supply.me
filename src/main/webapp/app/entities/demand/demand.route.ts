import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Demand, IDemand, DemandStatus } from 'app/shared/model/demand.model';
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
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((demand: HttpResponse<Demand>) => demand.body));
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
        const status = route.queryParams['status'] ? route.queryParams['status'] : null;
        return status as DemandStatus;
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
        path: 'demand/:id/comment',
        component: DemandChangeStatusPopupComponent,
        resolve: {
            demand: DemandResolve,
            status: StatusResolve
        },
        data: {
            authorities: ['ROLE_APPROVAL_LVL1', 'ROLE_APPROVAL_LVL2'],
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
