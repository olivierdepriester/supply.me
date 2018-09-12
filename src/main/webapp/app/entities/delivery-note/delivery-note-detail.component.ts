import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryNote } from 'app/shared/model/delivery-note.model';

@Component({
    selector: 'jhi-delivery-note-detail',
    templateUrl: './delivery-note-detail.component.html'
})
export class DeliveryNoteDetailComponent implements OnInit {
    deliveryNote: IDeliveryNote;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deliveryNote }) => {
            this.deliveryNote = deliveryNote;
        });
    }

    previousState() {
        window.history.back();
    }
}
