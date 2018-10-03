import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { MaterialSelectorComponent } from './material-selector.component';
import { SupplyMeSharedCommonModule } from 'app/shared';
import { AbstractSelectorModule } from 'app/entities/component/abstract-selector';
@NgModule({
    imports: [CommonModule, AutoCompleteModule, AbstractSelectorModule, SupplyMeSharedCommonModule],
    declarations: [MaterialSelectorComponent],
    entryComponents: [MaterialSelectorComponent],
    exports: [MaterialSelectorComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaterialSelectorModule {}
