import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SupplyMeSharedModule } from '../../../shared';
import { TreeModule } from 'primeng/components/tree/tree';
import { CategorySelectorComponent } from './';

@NgModule({
    imports: [CommonModule, SupplyMeSharedModule, TreeModule],
    declarations: [CategorySelectorComponent],
    entryComponents: [CategorySelectorComponent],
    exports: [CategorySelectorComponent]
})
export class CategorySelectorModule {}
