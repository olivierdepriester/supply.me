import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import {
    MaterialComponent,
    MaterialDetailComponent,
    MaterialStatisticsComponent,
    MaterialUpdateComponent,
    MaterialDeletePopupComponent,
    MaterialDeleteDialogComponent,
    materialRoute,
    materialPopupRoute
} from './';
import { CategorySelectorModule } from '../component/category-selector';
import { NgxChartsModule } from '@swimlane/ngx-charts';

const ENTITY_STATES = [...materialRoute, ...materialPopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, CategorySelectorModule, NgxChartsModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialComponent,
        MaterialDetailComponent,
        MaterialUpdateComponent,
        MaterialDeleteDialogComponent,
        MaterialDeletePopupComponent,
        MaterialStatisticsComponent
    ],
    entryComponents: [
        MaterialComponent,
        MaterialUpdateComponent,
        MaterialDeleteDialogComponent,
        MaterialDeletePopupComponent,
        MaterialStatisticsComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeMaterialModule {}
