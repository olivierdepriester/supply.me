import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Material } from 'app/shared/model/material.model';
import { MaterialService } from './material.service';
import { MaterialComponent } from './material.component';
import { MaterialDetailComponent } from './material-detail.component';
import { MaterialUpdateComponent } from './material-update.component';
import { MaterialDeletePopupComponent } from './material-delete-dialog.component';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialAvailabilityService } from '../material-availability';
import { IMaterialAvailability } from 'app/shared/model/material-availability.model';

@Injectable({ providedIn: 'root' })
export class MaterialResolve implements Resolve<IMaterial> {
    constructor(private service: MaterialService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Material> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Material>) => response.ok),
                map((material: HttpResponse<Material>) => material.body)
            );
        }
        return of(new Material());
    }
}

@Injectable({ providedIn: 'root' })
export class MaterialAvailabilitiesResolve implements Resolve<IMaterialAvailability[]> {
    constructor(private service: MaterialAvailabilityService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMaterialAvailability[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.findForMaterial(id).pipe(
                filter((response: HttpResponse<IMaterialAvailability[]>) => response.ok),
                map((material: HttpResponse<IMaterialAvailability[]>) => material.body)
            );
        }
        return of([]);
    }
}

export const materialRoute: Routes = [
    {
        path: 'material',
        component: MaterialComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.material.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material/:id/view',
        component: MaterialDetailComponent,
        resolve: {
            material: MaterialResolve,
            availabilities: MaterialAvailabilitiesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.material.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material/new',
        component: MaterialUpdateComponent,
        resolve: {
            material: MaterialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.material.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material/:id/edit',
        component: MaterialUpdateComponent,
        resolve: {
            material: MaterialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.material.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialPopupRoute: Routes = [
    {
        path: 'material/:id/delete',
        component: MaterialDeletePopupComponent,
        resolve: {
            material: MaterialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.material.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
