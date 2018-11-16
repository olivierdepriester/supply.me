import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceLine } from 'app/shared/model/invoice-line.model';

@Component({
    selector: 'jhi-invoice-line-detail',
    templateUrl: './invoice-line-detail.component.html'
})
export class InvoiceLineDetailComponent implements OnInit {
    invoiceLine: IInvoiceLine;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ invoiceLine }) => {
            this.invoiceLine = invoiceLine;
        });
    }

    previousState() {
        window.history.back();
    }
}
