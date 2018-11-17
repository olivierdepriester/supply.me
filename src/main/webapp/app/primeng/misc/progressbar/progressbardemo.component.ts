import { Component, OnInit, OnDestroy } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';
import { MenuItem, Message } from 'primeng/components/common/api';
import { Subscription, Observable } from 'rxjs';
import { interval } from 'rxjs';
import { take } from 'rxjs/operators';

@Component({
    selector: 'jhi-progressbar',
    templateUrl: './progressbardemo.component.html',
    styles: []
})
export class ProgressBarDemoComponent implements OnInit, OnDestroy {
    activeIndex = 0;
    msgs: Message[] = [];

    value: number;
    interval$: Subscription;

    ngOnInit() {
        const myInterval = interval(800).pipe(take(100));
        this.interval$ = myInterval.subscribe(
            x => (this.value = x + 1),
            () => {
                /** no error handling */
            },
            () => (this.msgs = [{ severity: 'info', summary: 'Success', detail: 'Process completed' }])
        );
    }

    ngOnDestroy() {
        this.interval$.unsubscribe();
    }

    onChangeStep(label: string) {
        this.msgs.length = 0;
        this.msgs.push({ severity: 'info', summary: label });
    }
}
