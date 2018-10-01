import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { ProjectSelectorComponent } from './project-selector.component';
import { SupplyMeSharedCommonModule } from 'app/shared';
@NgModule({
    imports: [CommonModule, AutoCompleteModule, SupplyMeSharedCommonModule],
    declarations: [ProjectSelectorComponent],
    entryComponents: [ProjectSelectorComponent],
    exports: [ProjectSelectorComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProjectSelectorModule {}
