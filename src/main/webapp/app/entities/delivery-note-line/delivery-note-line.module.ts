import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import {
    DeliveryNoteLineComponent,
    DeliveryNoteLineDetailComponent,
    DeliveryNoteLineUpdateComponent,
    DeliveryNoteLineDeletePopupComponent,
    DeliveryNoteLineDeleteDialogComponent,
    deliveryNoteLineRoute,
    deliveryNoteLinePopupRoute
} from './';

const ENTITY_STATES = [...deliveryNoteLineRoute, ...deliveryNoteLinePopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DeliveryNoteLineComponent,
        DeliveryNoteLineDetailComponent,
        DeliveryNoteLineUpdateComponent,
        DeliveryNoteLineDeleteDialogComponent,
        DeliveryNoteLineDeletePopupComponent
    ],
    entryComponents: [
        DeliveryNoteLineComponent,
        DeliveryNoteLineUpdateComponent,
        DeliveryNoteLineDeleteDialogComponent,
        DeliveryNoteLineDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeDeliveryNoteLineModule {}
