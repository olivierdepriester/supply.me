import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandCategory } from 'app/shared/model/demand-category.model';

@Component({
    selector: 'jhi-demand-category-detail',
    templateUrl: './demand-category-detail.component.html'
})
export class DemandCategoryDetailComponent implements OnInit {
    demandCategory: IDemandCategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demandCategory }) => {
            this.demandCategory = demandCategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
