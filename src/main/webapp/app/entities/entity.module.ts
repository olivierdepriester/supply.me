import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SupplyMeProjectModule } from './project/project.module';
import { SupplyMeMaterialModule } from './material/material.module';
import { SupplyMeSupplierModule } from './supplier/supplier.module';
import { SupplyMeMaterialAvailabilityModule } from './material-availability/material-availability.module';
import { SupplyMeDemandModule } from './demand/demand.module';
import { SupplyMePurchaseOrderModule } from './purchase-order/purchase-order.module';
import { SupplyMePurchaseOrderLineModule } from './purchase-order-line/purchase-order-line.module';
import { SupplyMeDeliveryNoteModule } from './delivery-note/delivery-note.module';
import { SupplyMeDeliveryNoteLineModule } from './delivery-note-line/delivery-note-line.module';
import { SupplyMeInvoiceModule } from './invoice/invoice.module';
import { SupplyMeInvoiceLineModule } from './invoice-line/invoice-line.module';
import { SupplyMeMutablePropertiesModule } from './mutable-properties/mutable-properties.module';
import { SupplyMeMaterialCategoryModule } from './material-category/material-category.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        SupplyMeProjectModule,
        SupplyMeMaterialModule,
        SupplyMeSupplierModule,
        SupplyMeMaterialAvailabilityModule,
        SupplyMeDemandModule,
        SupplyMePurchaseOrderModule,
        SupplyMePurchaseOrderLineModule,
        SupplyMeDeliveryNoteModule,
        SupplyMeDeliveryNoteLineModule,
        SupplyMeInvoiceModule,
        SupplyMeInvoiceLineModule,
        SupplyMeMutablePropertiesModule,
        SupplyMeMaterialCategoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeEntityModule {}
