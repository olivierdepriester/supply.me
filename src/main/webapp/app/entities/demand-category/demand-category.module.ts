import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import {
    DemandCategoryComponent,
    DemandCategoryDetailComponent,
    DemandCategoryUpdateComponent,
    DemandCategoryDeletePopupComponent,
    DemandCategoryDeleteDialogComponent,
    demandCategoryRoute,
    demandCategoryPopupRoute
} from './';

const ENTITY_STATES = [...demandCategoryRoute, ...demandCategoryPopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, SupplyMeAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DemandCategoryComponent,
        DemandCategoryDetailComponent,
        DemandCategoryUpdateComponent,
        DemandCategoryDeleteDialogComponent,
        DemandCategoryDeletePopupComponent
    ],
    entryComponents: [
        DemandCategoryComponent,
        DemandCategoryUpdateComponent,
        DemandCategoryDeleteDialogComponent,
        DemandCategoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeDemandCategoryModule {}
