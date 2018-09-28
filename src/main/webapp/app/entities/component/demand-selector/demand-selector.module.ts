import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { DemandSelectorComponent } from './demand-selector.component';
@NgModule({
    imports: [CommonModule, AutoCompleteModule],
    declarations: [DemandSelectorComponent],
    entryComponents: [DemandSelectorComponent],
    exports: [DemandSelectorComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DemandSelectorModule {}
