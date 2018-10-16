import { Component, forwardRef, ViewChild, Input } from '@angular/core';
import { PurchaseOrderLine } from '../../../shared/model/purchase-order-line.model';
import { AbstractSelectorComponent } from '../abstract-selector';
import { PurchaseOrderLineSelectorItem } from './purchase-order-line-selector.model';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { AutoComplete } from 'primeng/primeng';
import { PurchaseOrderLineService } from 'app/entities/purchase-order-line';
import { ISupplier, Supplier } from '../../../shared/model/supplier.model';

@Component({
    selector: 'jhi-purchase-order-line-selector',
    templateUrl: './purchase-order-line-selector.component.html',
    styles: [],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => PurchaseOrderLineSelectorComponent),
            multi: true
        }
    ]
})
export class PurchaseOrderLineSelectorComponent extends AbstractSelectorComponent<PurchaseOrderLine, PurchaseOrderLineSelectorItem> {
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;
    private _supplier: Supplier;

    get supplier(): Supplier {
        return this._supplier;
    }

    @Input('supplier')
    set supplier(value: Supplier) {
        this._supplier = value;
        console.log('force refresh to true');
        this.forceRefresh = true;
    }

    protected getNew(data: PurchaseOrderLine): PurchaseOrderLineSelectorItem {
        return new PurchaseOrderLineSelectorItem(data);
    }
    protected searchServiceFunction(myQuery: string): Observable<HttpResponse<PurchaseOrderLine[]>> {
        return this.service.search({ query: myQuery, supplierId: this.supplier == null ? null : this.supplier.id });
    }
    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }

    constructor(private service: PurchaseOrderLineService) {
        super();
    }
}
