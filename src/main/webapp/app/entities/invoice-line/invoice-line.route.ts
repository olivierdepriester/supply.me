import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { InvoiceLine } from 'app/shared/model/invoice-line.model';
import { InvoiceLineService } from './invoice-line.service';
import { InvoiceLineComponent } from './invoice-line.component';
import { InvoiceLineDetailComponent } from './invoice-line-detail.component';
import { InvoiceLineUpdateComponent } from './invoice-line-update.component';
import { InvoiceLineDeletePopupComponent } from './invoice-line-delete-dialog.component';
import { IInvoiceLine } from 'app/shared/model/invoice-line.model';

@Injectable({ providedIn: 'root' })
export class InvoiceLineResolve implements Resolve<IInvoiceLine> {
    constructor(private service: InvoiceLineService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((invoiceLine: HttpResponse<InvoiceLine>) => invoiceLine.body));
        }
        return of(new InvoiceLine());
    }
}

export const invoiceLineRoute: Routes = [
    {
        path: 'invoice-line',
        component: InvoiceLineComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'supplyMeApp.invoiceLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-line/:id/view',
        component: InvoiceLineDetailComponent,
        resolve: {
            invoiceLine: InvoiceLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.invoiceLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-line/new',
        component: InvoiceLineUpdateComponent,
        resolve: {
            invoiceLine: InvoiceLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.invoiceLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'invoice-line/:id/edit',
        component: InvoiceLineUpdateComponent,
        resolve: {
            invoiceLine: InvoiceLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.invoiceLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const invoiceLinePopupRoute: Routes = [
    {
        path: 'invoice-line/:id/delete',
        component: InvoiceLineDeletePopupComponent,
        resolve: {
            invoiceLine: InvoiceLineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'supplyMeApp.invoiceLine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
