import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';
import { SupplyMeSharedCommonModule } from 'app/shared';
@NgModule({
    imports: [CommonModule, AutoCompleteModule, SupplyMeSharedCommonModule]
})
export class AbstractSelectorModule {}
