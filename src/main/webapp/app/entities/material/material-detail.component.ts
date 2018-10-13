import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from './material.service';
import { HttpResponse } from '@angular/common/http';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-material-detail',
    templateUrl: './material-detail.component.html',
    styleUrls: ['./material.scss']
})
export class MaterialDetailComponent implements OnInit {
    material: IMaterial;

    constructor(private activatedRoute: ActivatedRoute, private materialService: MaterialService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ material }) => {
            this.material = material;
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
