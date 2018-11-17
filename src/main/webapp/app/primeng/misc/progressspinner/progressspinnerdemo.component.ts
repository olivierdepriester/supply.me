import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/components/common/api';

@Component({
    selector: 'jhi-progressspinner',
    templateUrl: './progressspinnerdemo.component.html',
    styles: []
})
export class ProgressSpinnerDemoComponent implements OnInit {
    activeIndex = 0;
    msgs: Message[] = [];

    ngOnInit() {}

    onChangeStep(label: string) {
        this.msgs.length = 0;
        this.msgs.push({ severity: 'info', summary: label });
    }
}
