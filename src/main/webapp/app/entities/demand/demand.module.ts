import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { MultiSelectModule } from 'primeng/components/multiselect/multiselect';
import { CalendarModule } from 'primeng/components/calendar/calendar';
import { MaterialSelectorModule } from 'app/entities/component/material-selector';
import { ProjectSelectorModule } from 'app/entities/component/project-selector';
import { UserSelectorModule } from 'app/entities/component/user-selector';
import { CategorySelectorModule } from 'app/entities/component/category-selector';
import { SupplierSelectorModule } from 'app/entities/component/supplier-selector';
import {
    DemandComponent,
    DemandDetailComponent,
    DemandUpdateComponent,
    DemandDeletePopupComponent,
    DemandDeleteDialogComponent,
    demandRoute,
    demandPopupRoute,
    MaterialTemporaryDialogComponent,
    MaterialTemporaryPopupComponent,
    DemandChangeStatusDialogComponent,
    DemandChangeStatusPopupComponent
} from './';

const ENTITY_STATES = [...demandRoute, ...demandPopupRoute];

@NgModule({
    imports: [
        SupplyMeSharedModule,
        SupplyMeAdminModule,
        AutoCompleteModule,
        MultiSelectModule,
        CalendarModule,
        MaterialSelectorModule,
        ProjectSelectorModule,
        UserSelectorModule,
        CategorySelectorModule,
        SupplierSelectorModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DemandComponent,
        DemandDetailComponent,
        DemandUpdateComponent,
        DemandDeleteDialogComponent,
        DemandDeletePopupComponent,
        MaterialTemporaryDialogComponent,
        MaterialTemporaryPopupComponent,
        DemandChangeStatusDialogComponent,
        DemandChangeStatusPopupComponent
    ],
    entryComponents: [
        DemandComponent,
        DemandUpdateComponent,
        DemandDeleteDialogComponent,
        DemandDeletePopupComponent,
        MaterialTemporaryDialogComponent,
        MaterialTemporaryPopupComponent,
        DemandChangeStatusDialogComponent,
        DemandChangeStatusPopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeDemandModule {}
