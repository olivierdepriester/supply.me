import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from './material.service';
import { IMaterialAvailability } from 'app/shared/model/material-availability.model';

@Component({
    selector: 'jhi-material-detail',
    templateUrl: './material-detail.component.html',
    styleUrls: ['./material.scss']
})
export class MaterialDetailComponent implements OnInit {
    material: IMaterial;
    availabilities: IMaterialAvailability[] = [];
    minPrice: number;
    maxPrice: number;

    constructor(private activatedRoute: ActivatedRoute, private materialService: MaterialService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ material, availabilities }) => {
            this.material = material;
            this.availabilities = availabilities;
            this.minPrice = this.availabilities.map(a => a.purchasePrice).reduce((a, b) => (a < b ? a : b));
            this.maxPrice = this.availabilities.map(a => a.purchasePrice).reduce((a, b) => (a > b ? a : b));
        });
    }

    validate() {
        this.material.temporary = false;
        this.materialService.update(this.material).subscribe((res: HttpResponse<IMaterial>) => (this.material = res.body));
    }

    previousState() {
        window.history.back();
    }
}
