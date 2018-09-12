import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialAvailability } from 'app/shared/model/material-availability.model';
import { MaterialAvailabilityService } from './material-availability.service';
import { MaterialAvailabilityComponent } from './material-availability.component';
import { MaterialAvailabilityDetailComponent } from './material-availability-detail.component';
import { MaterialAvailabilityUpdateComponent } from './material-availability-update.component';
import { MaterialAvailabilityDeletePopupComponent } from './material-availability-delete-dialog.component';
import { IMaterialAvailability } from 'app/shared/model/material-availability.model';

@Injectable({ providedIn: 'root' })
export class MaterialAvailabilityResolve implements Resolve<IMaterialAvailability> {
    constructor(private service: MaterialAvailabilityService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((materialAvailability: HttpResponse<MaterialAvailability>) => materialAvailability.body));
        }
        return of(new MaterialAvailability());
    }
}

export const materialAvailabilityRoute: Routes = [
    {
        path: 'material-availability',
        component: MaterialAvailabilityComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'supplyMeApp.materialAvailability.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-availability/:id/view',
        component: MaterialAvailabilityDetailComponent,
        resolve: {
            materialAvailability: MaterialAvailabilityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialAvailability.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-availability/new',
        component: MaterialAvailabilityUpdateComponent,
        resolve: {
            materialAvailability: MaterialAvailabilityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialAvailability.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-availability/:id/edit',
        component: MaterialAvailabilityUpdateComponent,
        resolve: {
            materialAvailability: MaterialAvailabilityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialAvailability.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialAvailabilityPopupRoute: Routes = [
    {
        path: 'material-availability/:id/delete',
        component: MaterialAvailabilityDeletePopupComponent,
        resolve: {
            materialAvailability: MaterialAvailabilityResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialAvailability.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
