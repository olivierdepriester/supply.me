import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import {
    PurchaseOrderComponent,
    PurchaseOrderDetailComponent,
    PurchaseOrderUpdateComponent,
    PurchaseOrderDeletePopupComponent,
    PurchaseOrderDeleteDialogComponent,
    purchaseOrderRoute,
    purchaseOrderPopupRoute
} from './';

const ENTITY_STATES = [...purchaseOrderRoute, ...purchaseOrderPopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, SupplyMeAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PurchaseOrderComponent,
        PurchaseOrderDetailComponent,
        PurchaseOrderUpdateComponent,
        PurchaseOrderDeleteDialogComponent,
        PurchaseOrderDeletePopupComponent
    ],
    entryComponents: [
        PurchaseOrderComponent,
        PurchaseOrderUpdateComponent,
        PurchaseOrderDeleteDialogComponent,
        PurchaseOrderDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMePurchaseOrderModule {}
