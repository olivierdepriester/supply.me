import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { PurchaseOrderLineService } from './purchase-order-line.service';
import { PurchaseOrderLineComponent } from './purchase-order-line.component';
import { PurchaseOrderLineDetailComponent } from './purchase-order-line-detail.component';
import { PurchaseOrderLineUpdateComponent } from './purchase-order-line-update.component';
import { PurchaseOrderLineDeletePopupComponent } from './purchase-order-line-delete-dialog.component';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

@Injectable({ providedIn: 'root' })
export class PurchaseOrderLineResolve implements Resolve<IPurchaseOrderLine> {
    constructor(private service: PurchaseOrderLineService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PurchaseOrderLine> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PurchaseOrderLine>) => response.ok),
                map((purchaseOrderLine: HttpResponse<PurchaseOrderLine>) => purchaseOrderLine.body)
            );
        }
        return of(new PurchaseOrderLine());
    }
}

export const purchaseOrderLineRoute: Routes = [
    {
        path: 'purchase-order-line',
        component: PurchaseOrderLineComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'supplyMeApp.purchaseOrderLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order-line/:id/view',
        component: PurchaseOrderLineDetailComponent,
        resolve: {
            purchaseOrderLine: PurchaseOrderLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrderLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order-line/new',
        component: PurchaseOrderLineUpdateComponent,
        resolve: {
            purchaseOrderLine: PurchaseOrderLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrderLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order-line/:id/edit',
        component: PurchaseOrderLineUpdateComponent,
        resolve: {
            purchaseOrderLine: PurchaseOrderLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrderLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchaseOrderLinePopupRoute: Routes = [
    {
        path: 'purchase-order-line/:id/delete',
        component: PurchaseOrderLineDeletePopupComponent,
        resolve: {
            purchaseOrderLine: PurchaseOrderLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrderLine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
