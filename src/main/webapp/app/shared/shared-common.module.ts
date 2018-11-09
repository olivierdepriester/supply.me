import { NgModule } from '@angular/core';

import {
    SupplyMeSharedLibsModule,
    FindLanguageFromKeyPipe,
    CurrencyCustomPipe,
    DatetimeFormatPipe,
    DateFormatPipe,
    NullToDashPipe,
    FileSizePipe,
    SmSupplierPipe,
    SmProjectPipe,
    SmMaterialPipe,
    JhiAlertComponent,
    JhiAlertErrorComponent
} from './';
import { YesNoPipe } from './util/yes-no.pipe';

@NgModule({
    imports: [SupplyMeSharedLibsModule],
    declarations: [
        FindLanguageFromKeyPipe,
        CurrencyCustomPipe,
        DateFormatPipe,
        DatetimeFormatPipe,
        NullToDashPipe,
        FileSizePipe,
        YesNoPipe,
        SmProjectPipe,
        SmMaterialPipe,
        SmSupplierPipe,
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
        YesNoPipe,
        SmProjectPipe,
        SmMaterialPipe,
        SmSupplierPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class SupplyMeSharedCommonModule {}
