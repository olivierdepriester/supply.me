import { HttpResponse } from '@angular/common/http';
import { Component, forwardRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { SELECTOR_SIZE } from 'app/app.constants';
import { AbstractSelectorComponent } from 'app/entities/component/abstract-selector';
import { DepartmentService } from 'app/entities/department';
import { AutoComplete } from 'primeng/primeng';
import { Observable } from 'rxjs';
import * as dataModel from '../../../shared/model/department.model';
import { DepartmentSelectorItem } from './';

@Component({
    selector: 'jhi-department-selector',
    templateUrl: './department-selector.component.html',
    styles: [],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => DepartmentSelectorComponent),
            multi: true
        }
    ]
})
export class DepartmentSelectorComponent extends AbstractSelectorComponent<dataModel.IDepartment, DepartmentSelectorItem> {
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;

    protected getNew(data: dataModel.IDepartment): DepartmentSelectorItem {
        return new DepartmentSelectorItem(data);
    }
    protected searchServiceFunction(textQuery: string): Observable<HttpResponse<dataModel.IDepartment[]>> {
        return this.service.search({ query: textQuery, size: SELECTOR_SIZE, sort: ['code.keyword,asc', 'id'] });
    }
    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }

    constructor(private service: DepartmentService) {
        super();
    }
}
