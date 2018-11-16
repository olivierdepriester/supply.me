import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialCategory } from 'app/shared/model/material-category.model';

@Component({
    selector: 'jhi-material-category-detail',
    templateUrl: './material-category-detail.component.html'
})
export class MaterialCategoryDetailComponent implements OnInit {
    materialCategory: IMaterialCategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialCategory }) => {
            this.materialCategory = materialCategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
