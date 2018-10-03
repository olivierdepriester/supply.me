import { Component, forwardRef, ViewChild } from '@angular/core';
import { AbstractSelectorComponent } from '../abstract-selector';
import * as dataModel from '../../../shared/model/supplier.model';
import { SupplierSelectorItem } from 'app/entities/component/supplier-selector/supplier-selector.model';
import { SupplierService } from 'app/entities/supplier';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { SELECTOR_SIZE } from 'app/app.constants';
import { AutoComplete } from 'primeng/primeng';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
    selector: 'jhi-supplier-selector',
    templateUrl: './supplier-selector.component.html',
    styles: [],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => SupplierSelectorComponent),
            multi: true
        }
    ]
})
export class SupplierSelectorComponent extends AbstractSelectorComponent<dataModel.ISupplier, SupplierSelectorItem> {
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;

    protected searchServiceFunction(textQuery: string): Observable<HttpResponse<dataModel.ISupplier[]>> {
        return this.service.search({ query: textQuery, size: SELECTOR_SIZE, sort: ['referenceNumber.keyword,asc', 'id'] });
    }
    protected getNew(data: dataModel.ISupplier): SupplierSelectorItem {
        return new SupplierSelectorItem(data);
    }

    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }

    constructor(private service: SupplierService) {
        super();
    }
}
