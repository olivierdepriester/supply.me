import { HttpResponse } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { SELECTOR_SIZE } from 'app/app.constants';
import { AbstractSelectorComponent } from 'app/entities/component/abstract-selector';
import { DemandService } from 'app/entities/demand/demand.service';
import { AutoComplete } from 'primeng/primeng';
import { Observable } from 'rxjs';
import * as dataModel from '../../../shared/model/demand.model';
import { DemandSelectorItem } from './';

@Component({
    selector: 'jhi-demand-selector',
    templateUrl: './demand-selector.component.html',
    styles: []
})
export class DemandSelectorComponent extends AbstractSelectorComponent<dataModel.IDemand, DemandSelectorItem> {
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;

    protected getNew(data: dataModel.IDemand): DemandSelectorItem {
        return new DemandSelectorItem(data);
    }
    protected searchServiceFunction(textQuery: string): Observable<HttpResponse<dataModel.IDemand[]>> {
        return this.service.searchPurchasable({ query: textQuery, size: SELECTOR_SIZE });
    }
    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }
    constructor(private service: DemandService) {
        super();
    }
}
