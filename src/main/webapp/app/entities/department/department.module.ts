import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import { UserSelectorModule } from 'app/entities/component/user-selector';
import { ProjectSelectorModule } from 'app/entities/component/project-selector';
import {
    DepartmentComponent,
    DepartmentDetailComponent,
    DepartmentUpdateComponent,
    DepartmentDeletePopupComponent,
    DepartmentDeleteDialogComponent,
    departmentRoute,
    departmentPopupRoute
} from './';
import { Project } from 'app/shared/model/project.model';

const ENTITY_STATES = [...departmentRoute, ...departmentPopupRoute];

@NgModule({
    imports: [SupplyMeSharedModule, SupplyMeAdminModule, UserSelectorModule, ProjectSelectorModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DepartmentComponent,
        DepartmentDetailComponent,
        DepartmentUpdateComponent,
        DepartmentDeleteDialogComponent,
        DepartmentDeletePopupComponent
    ],
    entryComponents: [DepartmentComponent, DepartmentUpdateComponent, DepartmentDeleteDialogComponent, DepartmentDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeDepartmentModule {}
