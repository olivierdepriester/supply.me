import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialAvailability } from 'app/shared/model/material-availability.model';

@Component({
    selector: 'jhi-material-availability-detail',
    templateUrl: './material-availability-detail.component.html'
})
export class MaterialAvailabilityDetailComponent implements OnInit {
    materialAvailability: IMaterialAvailability;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialAvailability }) => {
            this.materialAvailability = materialAvailability;
        });
    }

    previousState() {
        window.history.back();
    }
}
