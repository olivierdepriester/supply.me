import { animate, style, transition, trigger } from '@angular/animations';
import { DatePipe } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IMaterial } from 'app/shared/model/material.model';
import { IStatistics } from 'app/shared/model/statistics.model';
import { StatisticsService } from '../statistics/statistics.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SmSupplierPipe } from 'app/shared';

@Component({
    selector: 'jhi-material-statistics',
    templateUrl: './material-statistics.component.html',
    // styleUrls: ['../statistics/statistics.scss'],
    changeDetection: ChangeDetectionStrategy.Default,
    animations: [
        trigger('animationState', [
            transition('* => void', [
                style({
                    opacity: 1,
                    transform: '*'
                }),
                animate(500, style({ opacity: 0, transform: 'scale(0)' }))
            ])
        ])
    ]
})
export class MaterialStatisticsComponent implements OnInit {
    dataPricePerMonth: IStatistics;
    _material: IMaterial;
    results: any[];

    constructor(private activatedRoute: ActivatedRoute, private statisticsService: StatisticsService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ material }) => {
            this.material = material;
        });
    }

    get material(): IMaterial {
        return this._material;
    }

    @Input('material')
    set material(v: IMaterial) {
        if (v != null) {
            this._material = v;
            this.statisticsService.getPriceEvolutionForMaterial(this.material.id).subscribe((res: HttpResponse<IStatistics>) => {
                const datePipe = new DatePipe('en');
                const supplierPipe = new SmSupplierPipe();
                this.dataPricePerMonth = res.body;
                this.results = [];
                this.dataPricePerMonth.series.forEach(s => {
                    const serie = { name: supplierPipe.transform(s.key as ISupplier), series: [] };
                    s.dataPoints.forEach(point => {
                        if (point.value != null) {
                            serie.series.push({
                                value: point.value,
                                min: point.minValue,
                                max: point.maxValue,
                                name: datePipe.transform(point.name as Date, 'yyyy-MM', '+0800', 'en')
                            });
                        }
                    });
                    this.results.push(serie);
                });
            });
        }
    }
}
