import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from 'app/shared';
import { SupplyMeAdminModule } from 'app/admin/admin.module';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { MaterialSelectorModule } from 'app/entities/component/material-selector';
import { ProjectSelectorModule } from 'app/entities/component/project-selector';
import {
    DemandComponent,
    DemandDetailComponent,
    DemandUpdateComponent,
    DemandDeletePopupComponent,
    DemandDeleteDialogComponent,
    demandRoute,
    demandPopupRoute
} from './';

const ENTITY_STATES = [...demandRoute, ...demandPopupRoute];

@NgModule({
    imports: [
        SupplyMeSharedModule,
        SupplyMeAdminModule,
        AutoCompleteModule,
        MaterialSelectorModule,
        ProjectSelectorModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [DemandComponent, DemandDetailComponent, DemandUpdateComponent, DemandDeleteDialogComponent, DemandDeletePopupComponent],
    entryComponents: [DemandComponent, DemandUpdateComponent, DemandDeleteDialogComponent, DemandDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeDemandModule {}
