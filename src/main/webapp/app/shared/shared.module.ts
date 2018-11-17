import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { SupplyMeSharedLibsModule, SupplyMeSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { SelectorRequiredDirective } from 'app/entities/component/selector-required.directive';

@NgModule({
    imports: [SupplyMeSharedLibsModule, SupplyMeSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, SelectorRequiredDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [SupplyMeSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, SelectorRequiredDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeSharedModule {
    static forRoot() {
        return {
            ngModule: SupplyMeSharedModule
        };
    }
}
