import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import {
    DeliveryNoteComponent,
    DeliveryNoteDetailComponent,
    DeliveryNoteUpdateComponent,
    DeliveryNoteDeletePopupComponent,
    DeliveryNoteDeleteDialogComponent,
    deliveryNoteRoute,
    deliveryNotePopupRoute
} from './';

const ENTITY_STATES = [...deliveryNoteRoute, ...deliveryNotePopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, SupplyMeAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DeliveryNoteComponent,
        DeliveryNoteDetailComponent,
        DeliveryNoteUpdateComponent,
        DeliveryNoteDeleteDialogComponent,
        DeliveryNoteDeletePopupComponent
    ],
    entryComponents: [
        DeliveryNoteComponent,
        DeliveryNoteUpdateComponent,
        DeliveryNoteDeleteDialogComponent,
        DeliveryNoteDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeDeliveryNoteModule {}
