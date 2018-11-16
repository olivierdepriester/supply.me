import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DemandCategory } from 'app/shared/model/demand-category.model';
import { DemandCategoryService } from './demand-category.service';
import { DemandCategoryComponent } from './demand-category.component';
import { DemandCategoryDetailComponent } from './demand-category-detail.component';
import { DemandCategoryUpdateComponent } from './demand-category-update.component';
import { DemandCategoryDeletePopupComponent } from './demand-category-delete-dialog.component';
import { IDemandCategory } from 'app/shared/model/demand-category.model';

@Injectable({ providedIn: 'root' })
export class DemandCategoryResolve implements Resolve<IDemandCategory> {
    constructor(private service: DemandCategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<DemandCategory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DemandCategory>) => response.ok),
                map((demandCategory: HttpResponse<DemandCategory>) => demandCategory.body)
            );
        }
        return of(new DemandCategory());
    }
}

export const demandCategoryRoute: Routes = [
    {
        path: 'demand-category',
        component: DemandCategoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demandCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand-category/:id/view',
        component: DemandCategoryDetailComponent,
        resolve: {
            demandCategory: DemandCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demandCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand-category/new',
        component: DemandCategoryUpdateComponent,
        resolve: {
            demandCategory: DemandCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demandCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'demand-category/:id/edit',
        component: DemandCategoryUpdateComponent,
        resolve: {
            demandCategory: DemandCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demandCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const demandCategoryPopupRoute: Routes = [
    {
        path: 'demand-category/:id/delete',
        component: DemandCategoryDeletePopupComponent,
        resolve: {
            demandCategory: DemandCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.demandCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
