import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';
import { DeliveryNoteLineService } from './delivery-note-line.service';
import { DeliveryNoteLineComponent } from './delivery-note-line.component';
import { DeliveryNoteLineDetailComponent } from './delivery-note-line-detail.component';
import { DeliveryNoteLineUpdateComponent } from './delivery-note-line-update.component';
import { DeliveryNoteLineDeletePopupComponent } from './delivery-note-line-delete-dialog.component';
import { IDeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';

@Injectable({ providedIn: 'root' })
export class DeliveryNoteLineResolve implements Resolve<IDeliveryNoteLine> {
    constructor(private service: DeliveryNoteLineService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<DeliveryNoteLine> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DeliveryNoteLine>) => response.ok),
                map((deliveryNoteLine: HttpResponse<DeliveryNoteLine>) => deliveryNoteLine.body)
            );
        }
        return of(new DeliveryNoteLine());
    }
}

export const deliveryNoteLineRoute: Routes = [
    {
        path: 'delivery-note-line',
        component: DeliveryNoteLineComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'supplyMeApp.deliveryNoteLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'delivery-note-line/:id/view',
        component: DeliveryNoteLineDetailComponent,
        resolve: {
            deliveryNoteLine: DeliveryNoteLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNoteLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'delivery-note-line/new',
        component: DeliveryNoteLineUpdateComponent,
        resolve: {
            deliveryNoteLine: DeliveryNoteLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNoteLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'delivery-note-line/:id/edit',
        component: DeliveryNoteLineUpdateComponent,
        resolve: {
            deliveryNoteLine: DeliveryNoteLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNoteLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const deliveryNoteLinePopupRoute: Routes = [
    {
        path: 'delivery-note-line/:id/delete',
        component: DeliveryNoteLineDeletePopupComponent,
        resolve: {
            deliveryNoteLine: DeliveryNoteLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNoteLine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
