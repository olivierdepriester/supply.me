import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MaterialCategory } from 'app/shared/model/material-category.model';
import { MaterialCategoryService } from './material-category.service';
import { MaterialCategoryComponent } from './material-category.component';
import { MaterialCategoryDetailComponent } from './material-category-detail.component';
import { MaterialCategoryUpdateComponent } from './material-category-update.component';
import { MaterialCategoryDeletePopupComponent } from './material-category-delete-dialog.component';
import { IMaterialCategory } from 'app/shared/model/material-category.model';

@Injectable({ providedIn: 'root' })
export class MaterialCategoryResolve implements Resolve<IMaterialCategory> {
    constructor(private service: MaterialCategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<MaterialCategory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<MaterialCategory>) => response.ok),
                map((materialCategory: HttpResponse<MaterialCategory>) => materialCategory.body)
            );
        }
        return of(new MaterialCategory());
    }
}

export const materialCategoryRoute: Routes = [
    {
        path: 'material-category',
        component: MaterialCategoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'supplyMeApp.materialCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-category/:id/view',
        component: MaterialCategoryDetailComponent,
        resolve: {
            materialCategory: MaterialCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-category/new',
        component: MaterialCategoryUpdateComponent,
        resolve: {
            materialCategory: MaterialCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-category/:id/edit',
        component: MaterialCategoryUpdateComponent,
        resolve: {
            materialCategory: MaterialCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialCategoryPopupRoute: Routes = [
    {
        path: 'material-category/:id/delete',
        component: MaterialCategoryDeletePopupComponent,
        resolve: {
            materialCategory: MaterialCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.materialCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
