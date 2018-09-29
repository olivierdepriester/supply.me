import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { DemandService } from 'app/entities/demand/demand.service';
import * as dataModel from '../../../shared/model/demand.model';
import { DemandSelectorItem } from './';
import { HttpResponse } from '@angular/common/http';
import { SELECTOR_SIZE } from 'app/app.constants';
import { AutoComplete } from 'primeng/primeng';

@Component({
    selector: 'jhi-demand-selector',
    templateUrl: './demand-selector.component.html',
    styles: []
})
export class DemandSelectorComponent implements OnInit {
    filteredItems: DemandSelectorItem[];
    @Input() selectedData: dataModel.IDemand;
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;
    private lastQuery = '';
    constructor(private service: DemandService) {}

    ngOnInit() {}

    searchData(event) {
        this.lastQuery = event.query;
        this.search();
    }

    private search() {
        // Search demands that can be transformed into a purchase
        this.service
            .searchPurchasable({ query: this.lastQuery, size: SELECTOR_SIZE })
            .subscribe((res: HttpResponse<dataModel.IDemand[]>) => {
                this.filteredItems = Array.from(res.body, demand => new DemandSelectorItem(demand));
            });
    }

    onSelect(event) {
        this.selectedData = event.data;
    }

    onFocus(event: any) {
        if (this.filteredItems == null && this.lastQuery === '') {
            // If no search before : focus send a search based on an empty query
            this.search();
        } else {
            // Otherwise refresh the list content with the latest results
            this.filteredItems = Array.from(this.filteredItems);
        }
        this.autoComplete.show();
    }

    onClear() {
        this.selectedData = null;
    }
}