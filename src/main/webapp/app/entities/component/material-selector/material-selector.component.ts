import { HttpResponse } from '@angular/common/http';
import { Component, forwardRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { SELECTOR_SIZE } from 'app/app.constants';
import { AbstractSelectorComponent } from 'app/entities/component/abstract-selector';
import { MaterialService } from 'app/entities/material/material.service';
import { AutoComplete } from 'primeng/primeng';
import { Observable } from 'rxjs';
import * as dataModel from '../../../shared/model/material.model';
import { MaterialSelectorItem } from './';

@Component({
    selector: 'jhi-material-selector',
    templateUrl: './material-selector.component.html',
    styleUrls: ['./material-selector.component.scss'],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => MaterialSelectorComponent),
            multi: true
        }
    ]
})
export class MaterialSelectorComponent extends AbstractSelectorComponent<dataModel.IMaterial, MaterialSelectorItem> {
    @ViewChild(AutoComplete)
    private autoComplete: AutoComplete;

    protected getNew(data: dataModel.IMaterial): MaterialSelectorItem {
        return new MaterialSelectorItem(data);
    }
    protected searchServiceFunction(textQuery: string): Observable<HttpResponse<dataModel.IMaterial[]>> {
        return this.service.search({ query: textQuery, size: SELECTOR_SIZE, sort: ['partNumber,asc', 'id'] });
    }
    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }

    constructor(private service: MaterialService) {
        super();
    }
}
