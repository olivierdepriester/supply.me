import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemand } from 'app/shared/model/demand.model';

@Component({
    selector: 'jhi-demand-detail',
    templateUrl: './demand-detail.component.html'
})
export class DemandDetailComponent implements OnInit {
    demand: IDemand;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand }) => {
            this.demand = demand;
        });
    }

    previousState() {
        window.history.back();
    }
}
