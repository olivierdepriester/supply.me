import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMutableProperties } from 'app/shared/model/mutable-properties.model';

@Component({
    selector: 'jhi-mutable-properties-detail',
    templateUrl: './mutable-properties-detail.component.html'
})
export class MutablePropertiesDetailComponent implements OnInit {
    mutableProperties: IMutableProperties;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mutableProperties }) => {
            this.mutableProperties = mutableProperties;
        });
    }

    previousState() {
        window.history.back();
    }
}
