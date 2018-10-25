import { NgModule } from '@angular/core';

import {
    SupplyMeSharedLibsModule,
    FindLanguageFromKeyPipe,
    CurrencyCustomPipe,
    DatetimeFormatPipe,
    DateFormatPipe,
    NullToDashPipe,
    JhiAlertComponent,
    JhiAlertErrorComponent
} from './';

@NgModule({
    imports: [SupplyMeSharedLibsModule],
    declarations: [
        FindLanguageFromKeyPipe,
        CurrencyCustomPipe,
        DateFormatPipe,
        DatetimeFormatPipe,
        NullToDashPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ],
    exports: [
        SupplyMeSharedLibsModule,
        FindLanguageFromKeyPipe,
        CurrencyCustomPipe,
        DateFormatPipe,
        DatetimeFormatPipe,
        NullToDashPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class SupplyMeSharedCommonModule {}
