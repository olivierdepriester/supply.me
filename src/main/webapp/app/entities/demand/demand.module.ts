import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { MultiSelectModule } from 'primeng/components/multiselect/multiselect';
import { CalendarModule } from 'primeng/components/calendar/calendar';
import { FileUploadModule } from 'primeng/fileupload';
import { MaterialSelectorModule } from 'app/entities/component/material-selector';
import { ProjectSelectorModule } from 'app/entities/component/project-selector';
import { DepartmentSelectorModule } from 'app/entities/component/department-selector';
import { UserSelectorModule } from 'app/entities/component/user-selector';
import { CategorySelectorModule } from 'app/entities/component/category-selector';
import { SupplierSelectorModule } from 'app/entities/component/supplier-selector';
import { DemandCategorySelectorModule } from 'app/entities/component/demand-category-selector';
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
    SupplierTemporaryDialogComponent,
    SupplierTemporaryPopupComponent,
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
        FileUploadModule,
        MaterialSelectorModule,
        DepartmentSelectorModule,
        ProjectSelectorModule,
        UserSelectorModule,
        CategorySelectorModule,
        SupplierSelectorModule,
        DemandCategorySelectorModule,
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
        SupplierTemporaryDialogComponent,
        SupplierTemporaryPopupComponent,
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
        SupplierTemporaryDialogComponent,
        SupplierTemporaryPopupComponent,
        DemandChangeStatusDialogComponent,
        DemandChangeStatusPopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeDemandModule {}
