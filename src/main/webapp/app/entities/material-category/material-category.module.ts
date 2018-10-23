import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import { CategorySelectorModule } from 'app/entities/component/category-selector';
import {
    MaterialCategoryComponent,
    MaterialCategoryDetailComponent,
    MaterialCategoryUpdateComponent,
    MaterialCategoryDeletePopupComponent,
    MaterialCategoryDeleteDialogComponent,
    materialCategoryRoute,
    materialCategoryPopupRoute
} from './';

const ENTITY_STATES = [...materialCategoryRoute, ...materialCategoryPopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, SupplyMeAdminModule, CategorySelectorModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialCategoryComponent,
        MaterialCategoryDetailComponent,
        MaterialCategoryUpdateComponent,
        MaterialCategoryDeleteDialogComponent,
        MaterialCategoryDeletePopupComponent
    ],
    entryComponents: [
        MaterialCategoryComponent,
        MaterialCategoryUpdateComponent,
        MaterialCategoryDeleteDialogComponent,
        MaterialCategoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeMaterialCategoryModule {}
