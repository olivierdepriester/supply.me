import { HttpResponse } from '@angular/common/http';
import { Component, ViewChild, forwardRef } from '@angular/core';
import { AutoComplete } from 'primeng/components/autocomplete/autocomplete';
import { Observable } from 'rxjs';
import * as dataModel from '../../../shared/model/demand-category.model';
import { DemandCategorySelectorItem } from './demand-category-selector.model';
import { AbstractSelectorComponent } from '../abstract-selector';
import { SELECTOR_SIZE } from 'app/app.constants';
import { DemandCategoryService } from 'app/entities/demand-category';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
    selector: 'jhi-demand-category-selector',
    templateUrl: './demand-category-selector.component.html',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => DemandCategorySelectorComponent),
            multi: true
        }
    ]
})
export class DemandCategorySelectorComponent extends AbstractSelectorComponent<dataModel.IDemandCategory, DemandCategorySelectorItem> {
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;

    protected getNew(data: dataModel.IDemandCategory): DemandCategorySelectorItem {
        return new DemandCategorySelectorItem(data);
    }
    protected searchServiceFunction(textQuery: string): Observable<HttpResponse<dataModel.IDemandCategory[]>> {
        return this.service.search({ query: textQuery, size: SELECTOR_SIZE });
    }
    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }
    constructor(private service: DemandCategoryService) {
        super();
    }
}
