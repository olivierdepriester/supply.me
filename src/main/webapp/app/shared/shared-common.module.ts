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
import { SmDepartmentPipe } from './util/entities/sm-department.pipe';
import { SmUserPipe } from './util/entities/sm-user.pipe';

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
        SmDepartmentPipe,
        SmProjectPipe,
        SmMaterialPipe,
        SmSupplierPipe,
        SmUserPipe,
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
        SmDepartmentPipe,
        SmMaterialPipe,
        SmProjectPipe,
        SmSupplierPipe,
        SmUserPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class SupplyMeSharedCommonModule {}
