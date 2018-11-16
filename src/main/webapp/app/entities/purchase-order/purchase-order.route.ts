import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from './purchase-order.service';
import { PurchaseOrderComponent } from './purchase-order.component';
import { PurchaseOrderDetailComponent } from './purchase-order-detail.component';
import { PurchaseOrderUpdateComponent } from './purchase-order-update.component';
import { PurchaseOrderDeletePopupComponent } from './purchase-order-delete-dialog.component';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';

@Injectable({ providedIn: 'root' })
export class PurchaseOrderResolve implements Resolve<IPurchaseOrder> {
    constructor(private service: PurchaseOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PurchaseOrder> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PurchaseOrder>) => response.ok),
                map((purchaseOrder: HttpResponse<PurchaseOrder>) => purchaseOrder.body)
            );
        }
        return of(new PurchaseOrder());
    }
}

export const purchaseOrderRoute: Routes = [
    {
        path: 'purchase-order',
        component: PurchaseOrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order/:id/view',
        component: PurchaseOrderDetailComponent,
        resolve: {
            purchaseOrder: PurchaseOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order/new',
        component: PurchaseOrderUpdateComponent,
        resolve: {
            purchaseOrder: PurchaseOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order/:id/edit',
        component: PurchaseOrderUpdateComponent,
        resolve: {
            purchaseOrder: PurchaseOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchaseOrderPopupRoute: Routes = [
    {
        path: 'purchase-order/:id/delete',
        component: PurchaseOrderDeletePopupComponent,
        resolve: {
            purchaseOrder: PurchaseOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
