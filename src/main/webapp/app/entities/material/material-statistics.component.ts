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
import { Observable } from 'rxjs';
import * as shape from 'd3-shape';
import ChartUtils, { ColorSet } from 'app/shared/util/chart-utils';

export let colorSets = [
    {
        name: 'vivid',
        selectable: true,
        group: 'Ordinal',
        domain: ['#647c8a', '#3f51b5', '#2196f3', '#00b862', '#afdf0a', '#a7b61a', '#f3e562', '#ff9800', '#ff5722', '#ff4514']
    },
    {
        name: 'natural',
        selectable: true,
        group: 'Ordinal',
        domain: ['#bf9d76', '#e99450', '#d89f59', '#f2dfa7', '#a5d7c6', '#7794b1', '#afafaf', '#707160', '#ba9383', '#d9d5c3']
    },
    {
        name: 'cool',
        selectable: true,
        group: 'Ordinal',
        domain: ['#a8385d', '#7aa3e5', '#a27ea8', '#aae3f5', '#adcded', '#a95963', '#8796c0', '#7ed3ed', '#50abcc', '#ad6886']
    },
    {
        name: 'fire',
        selectable: true,
        group: 'Ordinal',
        domain: ['#ff3d00', '#bf360c', '#ff8f00', '#ff6f00', '#ff5722', '#e65100', '#ffca28', '#ffab00']
    },
    {
        name: 'solar',
        selectable: true,
        group: 'Continuous',
        domain: ['#fff8e1', '#ffecb3', '#ffe082', '#ffd54f', '#ffca28', '#ffc107', '#ffb300', '#ffa000', '#ff8f00', '#ff6f00']
    },
    {
        name: 'air',
        selectable: true,
        group: 'Continuous',
        domain: ['#e1f5fe', '#b3e5fc', '#81d4fa', '#4fc3f7', '#29b6f6', '#03a9f4', '#039be5', '#0288d1', '#0277bd', '#01579b']
    },
    {
        name: 'aqua',
        selectable: true,
        group: 'Continuous',
        domain: ['#e0f7fa', '#b2ebf2', '#80deea', '#4dd0e1', '#26c6da', '#00bcd4', '#00acc1', '#0097a7', '#00838f', '#006064']
    },
    {
        name: 'flame',
        selectable: false,
        group: 'Ordinal',
        domain: ['#A10A28', '#D3342D', '#EF6D49', '#FAAD67', '#FDDE90', '#DBED91', '#A9D770', '#6CBA67', '#2C9653', '#146738']
    },
    {
        name: 'ocean',
        selectable: false,
        group: 'Ordinal',
        domain: ['#1D68FB', '#33C0FC', '#4AFFFE', '#AFFFFF', '#FFFC63', '#FDBD2D', '#FC8A25', '#FA4F1E', '#FA141B', '#BA38D1']
    },
    {
        name: 'forest',
        selectable: false,
        group: 'Ordinal',
        domain: ['#55C22D', '#C1F33D', '#3CC099', '#AFFFFF', '#8CFC9D', '#76CFFA', '#BA60FB', '#EE6490', '#C42A1C', '#FC9F32']
    },
    {
        name: 'horizon',
        selectable: false,
        group: 'Ordinal',
        domain: ['#2597FB', '#65EBFD', '#99FDD0', '#FCEE4B', '#FEFCFA', '#FDD6E3', '#FCB1A8', '#EF6F7B', '#CB96E8', '#EFDEE0']
    },
    {
        name: 'neons',
        selectable: false,
        group: 'Ordinal',
        domain: ['#FF3333', '#FF33FF', '#CC33FF', '#0000FF', '#33CCFF', '#33FFFF', '#33FF66', '#CCFF33', '#FFCC00', '#FF6600']
    },
    {
        name: 'picnic',
        selectable: false,
        group: 'Ordinal',
        domain: ['#FAC51D', '#66BD6D', '#FAA026', '#29BB9C', '#E96B56', '#55ACD2', '#B7332F', '#2C83C9', '#9166B8', '#92E7E8']
    },
    {
        name: 'night',
        selectable: false,
        group: 'Ordinal',
        domain: [
            '#2B1B5A',
            '#501356',
            '#183356',
            '#28203F',
            '#391B3C',
            '#1E2B3C',
            '#120634',
            '#2D0432',
            '#051932',
            '#453080',
            '#75267D',
            '#2C507D',
            '#4B3880',
            '#752F7D',
            '#35547D'
        ]
    },
    {
        name: 'nightLights',
        selectable: false,
        group: 'Ordinal',
        domain: [
            '#4e31a5',
            '#9c25a7',
            '#3065ab',
            '#57468b',
            '#904497',
            '#46648b',
            '#32118d',
            '#a00fb3',
            '#1052a2',
            '#6e51bd',
            '#b63cc3',
            '#6c97cb',
            '#8671c1',
            '#b455be',
            '#7496c3'
        ]
    }
];

@Component({
    selector: 'jhi-material-statistics',
    templateUrl: './material-statistics.component.html',
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
    private _material: IMaterial;
    private datePipe = new DatePipe('en');
    private supplierPipe = new SmSupplierPipe();
    results: any[] = [];
    priceEvolution = 'priceEvolution';
    quantityEvolution = 'quantityEvolution';
    curvePrice = shape.curveMonotoneX;
    curveQuantity = shape.curveStep; // shape.curveMonotoneX;
    colorScheme: ColorSet;
    schemeType: string;
    selectedColorScheme: string;

    xScaleMin: Date;
    xScaleMax: Date;

    constructor(private activatedRoute: ActivatedRoute, private statisticsService: StatisticsService) {}

    ngOnInit() {
        this.colorScheme = ChartUtils.getDefaultColorSet();
        this.schemeType = ChartUtils.getDefaultSchemeType();
        this.activatedRoute.data.subscribe(({ material }) => {
            this.material = material;
        });
    }

    get material(): IMaterial {
        return this._material;
    }

    @Input('material')
    set material(material: IMaterial) {
        if (material != null) {
            this._material = material;
            this.fillResultsFromRequest(this.statisticsService.getPriceEvolutionForMaterial(this.material.id), this.priceEvolution, true);
            this.fillResultsFromRequest(
                this.statisticsService.getQuantityOrderedForMaterial(this.material.id),
                this.quantityEvolution,
                false
            );
        }
    }

    private fillResultsFromRequest(query: Observable<HttpResponse<IStatistics>>, targetChart: string, initMinMax: boolean) {
        query.subscribe((res: HttpResponse<IStatistics>) => {
            this.results[targetChart] = [];
            this.xScaleMin = res.body.series
                .map(s => s.dataPoints.map(d => new Date(d.name)).reduce((c, d) => (c.getTime() < d.getTime() ? c : d)))
                .reduce((a, b) => (a.getTime() < b.getTime() ? a : b));
            this.xScaleMax = res.body.series
                .map(s => s.dataPoints.map(d => new Date(d.name)).reduce((e, f) => (e.getTime() > f.getTime() ? e : f)))
                .reduce((g, h) => (g.getTime() > h.getTime() ? g : h));
            res.body.series.forEach(s => {
                const serie = { name: this.supplierPipe.transform(s.key as ISupplier), series: [] };
                s.dataPoints.forEach(point => {
                    // if (point.value != null) {
                    serie.series.push({
                        value: point.value == null ? 0 : point.value,
                        // min: initMinMax ? point.minValue : null,
                        // max: initMinMax ? point.maxValue : null,
                        name: new Date(point.name) // this.datePipe.transform(point.name as Date, 'yyyy-MM', '+0800', 'en')
                    });
                    // }
                });
                this.results[targetChart].push(serie);
            });
        });
    }

    calendarAxisTickFormatting(mondayString: Date) {
        return new DatePipe('en').transform(mondayString, 'yyyy-MM', '+0800', 'en');
    }
}
