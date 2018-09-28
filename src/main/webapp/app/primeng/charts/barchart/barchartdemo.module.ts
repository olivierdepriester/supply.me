import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupplyMeSharedModule } from '../../../shared';
import { ChartModule } from 'primeng/primeng';

import { BarchartDemoComponent, barchartDemoRoute } from './';

const primeng_STATES = [barchartDemoRoute];

@NgModule({
    imports: [SupplyMeSharedModule, ChartModule, RouterModule.forRoot(primeng_STATES, { useHash: true })],
    declarations: [BarchartDemoComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeBarchartDemoModule {}
