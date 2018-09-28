import { NgModule } from '@angular/core';

import { SupplyMeSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { DateFormatPipe } from './util/date-format.pipe';
import { DatetimeFormatPipe } from './util/datetime-format.pipe';
@NgModule({
    imports: [SupplyMeSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, DateFormatPipe, DatetimeFormatPipe, JhiAlertComponent, JhiAlertErrorComponent],
    exports: [
        SupplyMeSharedLibsModule,
        FindLanguageFromKeyPipe,
        DateFormatPipe,
        DatetimeFormatPipe,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class SupplyMeSharedCommonModule {}
