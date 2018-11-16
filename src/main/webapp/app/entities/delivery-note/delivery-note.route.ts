import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DeliveryNote } from 'app/shared/model/delivery-note.model';
import { DeliveryNoteService } from './delivery-note.service';
import { DeliveryNoteComponent } from './delivery-note.component';
import { DeliveryNoteDetailComponent } from './delivery-note-detail.component';
import { DeliveryNoteUpdateComponent } from './delivery-note-update.component';
import { DeliveryNoteDeletePopupComponent } from './delivery-note-delete-dialog.component';
import { IDeliveryNote } from 'app/shared/model/delivery-note.model';

@Injectable({ providedIn: 'root' })
export class DeliveryNoteResolve implements Resolve<IDeliveryNote> {
    constructor(private service: DeliveryNoteService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<DeliveryNote> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DeliveryNote>) => response.ok),
                map((deliveryNote: HttpResponse<DeliveryNote>) => deliveryNote.body)
            );
        }
        return of(new DeliveryNote());
    }
}

export const deliveryNoteRoute: Routes = [
    {
        path: 'delivery-note',
        component: DeliveryNoteComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNote.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'delivery-note/:id/view',
        component: DeliveryNoteDetailComponent,
        resolve: {
            deliveryNote: DeliveryNoteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNote.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'delivery-note/new',
        component: DeliveryNoteUpdateComponent,
        resolve: {
            deliveryNote: DeliveryNoteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNote.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'delivery-note/:id/edit',
        component: DeliveryNoteUpdateComponent,
        resolve: {
            deliveryNote: DeliveryNoteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const deliveryNotePopupRoute: Routes = [
    {
        path: 'delivery-note/:id/delete',
        component: DeliveryNoteDeletePopupComponent,
        resolve: {
            deliveryNote: DeliveryNoteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.deliveryNote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
