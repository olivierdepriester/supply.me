import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { SupplyMeSharedModule } from '../../../shared';
import { GrowlModule } from 'primeng/primeng';
import { MenuModule } from 'primeng/components/menu/menu';
import { ButtonModule } from 'primeng/components/button/button';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { MenuDemoComponent, menuDemoRoute } from './';

const primeng_STATES = [menuDemoRoute];

@NgModule({
    imports: [
        SupplyMeSharedModule,
        CommonModule,
        BrowserAnimationsModule,
        MenuModule,
        GrowlModule,
        ButtonModule,
        WizardModule,
        RouterModule.forRoot(primeng_STATES, { useHash: true })
    ],
    declarations: [MenuDemoComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeMenuDemoModule {}
