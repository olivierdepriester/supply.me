import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import {
    MaterialAvailabilityComponent,
    MaterialAvailabilityDetailComponent,
    MaterialAvailabilityUpdateComponent,
    MaterialAvailabilityDeletePopupComponent,
    MaterialAvailabilityDeleteDialogComponent,
    materialAvailabilityRoute,
    materialAvailabilityPopupRoute
} from './';

const ENTITY_STATES = [...materialAvailabilityRoute, ...materialAvailabilityPopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialAvailabilityComponent,
        MaterialAvailabilityDetailComponent,
        MaterialAvailabilityUpdateComponent,
        MaterialAvailabilityDeleteDialogComponent,
        MaterialAvailabilityDeletePopupComponent
    ],
    entryComponents: [
        MaterialAvailabilityComponent,
        MaterialAvailabilityUpdateComponent,
        MaterialAvailabilityDeleteDialogComponent,
        MaterialAvailabilityDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeMaterialAvailabilityModule {}
