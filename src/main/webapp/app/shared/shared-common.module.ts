import { NgModule } from '@angular/core';

import { SupplyMeSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { DateFormatPipe } from './util/date-format.pipe';

@NgModule({
    imports: [SupplyMeSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, DateFormatPipe, JhiAlertComponent, JhiAlertErrorComponent],
    exports: [SupplyMeSharedLibsModule, FindLanguageFromKeyPipe, DateFormatPipe, JhiAlertComponent, JhiAlertErrorComponent]
})
export class SupplyMeSharedCommonModule {}
