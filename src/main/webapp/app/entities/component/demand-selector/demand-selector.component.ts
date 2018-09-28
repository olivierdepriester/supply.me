import { Component, OnInit, Input } from '@angular/core';
import { DemandService } from 'app/entities/demand/demand.service';
import * as demandModel from '../../../shared/model/demand.model';
import { DemandSelectorItem } from './';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-demand-selector',
    templateUrl: './demand-selector.component.html',
    styles: []
})
export class DemandSelectorComponent implements OnInit {
    results: DemandSelectorItem[];
    @Input() selectedDemand: demandModel.IDemand;

    constructor(private demandService: DemandService) {}

    ngOnInit() {}

    searchDemands(event) {
        // Search demands that can be transformed into a purchase
        this.demandService.searchPurchasable({ query: event.query }).subscribe((res: HttpResponse<demandModel.IDemand[]>) => {
            this.results = Array.from(res.body, demand => new DemandSelectorItem(demand));
        });
    }

    onSelect(event) {
        this.selectedDemand = event.demand;
    }

    onUnselect() {
        this.selectedDemand = null;
    }

    onClear() {
        this.selectedDemand = null;
    }
}
