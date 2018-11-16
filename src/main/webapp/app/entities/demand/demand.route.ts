import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Demand } from 'app/shared/model/demand.model';
import { DemandService } from './demand.service';
import { DemandComponent } from './demand.component';
import { DemandDetailComponent } from './demand-detail.component';
import { DemandUpdateComponent } from './demand-update.component';
import { DemandDeletePopupComponent } from './demand-delete-dialog.component';
import { IDemand } from 'app/shared/model/demand.model';

@Injectable({ providedIn: 'root' })
export class DemandResolve implements Resolve<IDemand> {
    constructor(private service: DemandService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Demand> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Demand>) => response.ok),
                map((demand: HttpResponse<Demand>) => demand.body)
            );
        }
        return of(new Demand());
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
            demand: DemandResolve
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
    }
];
