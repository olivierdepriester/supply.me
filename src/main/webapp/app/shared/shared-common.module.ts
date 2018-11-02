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
import { FileSizePipe } from './util/file-size.pipe';

@NgModule({
    imports: [SupplyMeSharedLibsModule],
    declarations: [
        FindLanguageFromKeyPipe,
        CurrencyCustomPipe,
        DateFormatPipe,
        DatetimeFormatPipe,
        NullToDashPipe,
        FileSizePipe,
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
        FileSizePipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class SupplyMeSharedCommonModule {}
