import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MaterialService } from 'app/entities/material/material.service';
import * as dataModel from '../../../shared/model/material.model';
import { MaterialSelectorItem } from './';
import { HttpResponse } from '@angular/common/http';
import { SELECTOR_SIZE } from 'app/app.constants';
import { AutoComplete } from 'primeng/primeng';

@Component({
    selector: 'jhi-material-selector',
    templateUrl: './material-selector.component.html',
    styles: []
})
export class MaterialSelectorComponent implements OnInit {
    filteredItems: MaterialSelectorItem[];
    @Input() selectedData: dataModel.IMaterial;
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;
    private lastQuery = '';
    constructor(private service: MaterialService) {}

    ngOnInit() {}

    searchData(event) {
        this.lastQuery = event.query;
        this.search();
    }

    private search() {
        // Search material
        this.service
            .search({ query: this.lastQuery, size: SELECTOR_SIZE, sort: ['partNumber.keyword,asc', 'id'] })
            .subscribe((res: HttpResponse<dataModel.IMaterial[]>) => {
                this.filteredItems = Array.from(res.body, data => new MaterialSelectorItem(data));
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
