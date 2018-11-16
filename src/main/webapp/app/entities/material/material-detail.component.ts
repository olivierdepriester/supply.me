import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterial } from 'app/shared/model/material.model';

@Component({
    selector: 'jhi-material-detail',
    templateUrl: './material-detail.component.html'
})
export class MaterialDetailComponent implements OnInit {
    material: IMaterial;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ material }) => {
            this.material = material;
        });
    }

    previousState() {
        window.history.back();
    }
}
