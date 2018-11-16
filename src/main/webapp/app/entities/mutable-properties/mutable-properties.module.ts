import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import {
    MutablePropertiesComponent,
    MutablePropertiesDetailComponent,
    MutablePropertiesUpdateComponent,
    MutablePropertiesDeletePopupComponent,
    MutablePropertiesDeleteDialogComponent,
    mutablePropertiesRoute,
    mutablePropertiesPopupRoute
} from './';

const ENTITY_STATES = [...mutablePropertiesRoute, ...mutablePropertiesPopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MutablePropertiesComponent,
        MutablePropertiesDetailComponent,
        MutablePropertiesUpdateComponent,
        MutablePropertiesDeleteDialogComponent,
        MutablePropertiesDeletePopupComponent
    ],
    entryComponents: [
        MutablePropertiesComponent,
        MutablePropertiesUpdateComponent,
        MutablePropertiesDeleteDialogComponent,
        MutablePropertiesDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeMutablePropertiesModule {}
