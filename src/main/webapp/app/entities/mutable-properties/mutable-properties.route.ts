import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MutableProperties } from 'app/shared/model/mutable-properties.model';
import { MutablePropertiesService } from './mutable-properties.service';
import { MutablePropertiesComponent } from './mutable-properties.component';
import { MutablePropertiesDetailComponent } from './mutable-properties-detail.component';
import { MutablePropertiesUpdateComponent } from './mutable-properties-update.component';
import { MutablePropertiesDeletePopupComponent } from './mutable-properties-delete-dialog.component';
import { IMutableProperties } from 'app/shared/model/mutable-properties.model';

@Injectable({ providedIn: 'root' })
export class MutablePropertiesResolve implements Resolve<IMutableProperties> {
    constructor(private service: MutablePropertiesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<MutableProperties> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<MutableProperties>) => response.ok),
                map((mutableProperties: HttpResponse<MutableProperties>) => mutableProperties.body)
            );
        }
        return of(new MutableProperties());
    }
}

export const mutablePropertiesRoute: Routes = [
    {
        path: 'mutable-properties',
        component: MutablePropertiesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.mutableProperties.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mutable-properties/:id/view',
        component: MutablePropertiesDetailComponent,
        resolve: {
            mutableProperties: MutablePropertiesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.mutableProperties.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mutable-properties/new',
        component: MutablePropertiesUpdateComponent,
        resolve: {
            mutableProperties: MutablePropertiesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.mutableProperties.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mutable-properties/:id/edit',
        component: MutablePropertiesUpdateComponent,
        resolve: {
            mutableProperties: MutablePropertiesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.mutableProperties.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mutablePropertiesPopupRoute: Routes = [
    {
        path: 'mutable-properties/:id/delete',
        component: MutablePropertiesDeletePopupComponent,
        resolve: {
            mutableProperties: MutablePropertiesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.mutableProperties.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
