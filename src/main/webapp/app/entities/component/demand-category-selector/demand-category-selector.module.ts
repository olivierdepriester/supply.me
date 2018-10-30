import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { DemandCategorySelectorComponent } from './';
import { SupplyMeSharedCommonModule } from 'app/shared';
import { AbstractSelectorModule } from 'app/entities/component/abstract-selector';
@NgModule({
    imports: [CommonModule, AutoCompleteModule, SupplyMeSharedCommonModule, AbstractSelectorModule],
    declarations: [DemandCategorySelectorComponent],
    entryComponents: [DemandCategorySelectorComponent],
    exports: [DemandCategorySelectorComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DemandCategorySelectorModule {}
