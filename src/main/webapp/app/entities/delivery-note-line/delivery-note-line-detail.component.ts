import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';

@Component({
    selector: 'jhi-delivery-note-line-detail',
    templateUrl: './delivery-note-line-detail.component.html'
})
export class DeliveryNoteLineDetailComponent implements OnInit {
    deliveryNoteLine: IDeliveryNoteLine;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deliveryNoteLine }) => {
            this.deliveryNoteLine = deliveryNoteLine;
        });
    }

    previousState() {
        window.history.back();
    }
}
