import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { SupplyMeSharedCommonModule } from 'app/shared';
import { AbstractSelectorModule } from '../abstract-selector';
import { UserSelectorComponent } from './user-selector.component';
import { AutoCompleteModule } from 'primeng/components/autocomplete/autocomplete';

@NgModule({
    imports: [CommonModule, AbstractSelectorModule, SupplyMeSharedCommonModule, AutoCompleteModule],
    declarations: [UserSelectorComponent],
    entryComponents: [UserSelectorComponent],
    exports: [UserSelectorComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UserSelectorModule {}
