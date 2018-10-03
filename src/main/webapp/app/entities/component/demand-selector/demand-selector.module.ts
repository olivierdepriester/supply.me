import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { DemandSelectorComponent } from './demand-selector.component';
import { SupplyMeSharedCommonModule } from 'app/shared';
import { AbstractSelectorModule } from 'app/entities/component/abstract-selector';
@NgModule({
    imports: [CommonModule, AutoCompleteModule, SupplyMeSharedCommonModule, AbstractSelectorModule],
    declarations: [DemandSelectorComponent],
    entryComponents: [DemandSelectorComponent],
    exports: [DemandSelectorComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DemandSelectorModule {}
