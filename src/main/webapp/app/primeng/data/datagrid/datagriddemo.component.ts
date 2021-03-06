import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';
import { Message } from 'primeng/components/common/api';
import { BrowserService } from './service/browser.service';
import Browser from './service/browser';

@Component({
    selector: 'jhi-datagrid',
    templateUrl: './datagriddemo.component.html',
    styles: []
})
export class DataGridDemoComponent implements OnInit {
    msgs: Message[] = [];
    activeIndex = 0;
    totalRecords = 100;

    basicBrowsers: Browser[];
    facetBrowsers: Browser[];
    paginationBrowsers: Browser[];
    lazyloadingBrowsers: Browser[];
    responsiveBrowsers: Browser[];
    advancedBrowsers: Browser[];

    selectedBrowser: Browser;

    displayDialog: boolean;

    constructor(private browserService: BrowserService) {}

    ngOnInit() {
        this.browserService.getBrowsers().subscribe((browsers: any) => (this.basicBrowsers = browsers.data.slice(0, 12)));
        this.browserService.getBrowsers().subscribe((browsers: any) => (this.facetBrowsers = browsers.data.slice(0, 12)));
        this.browserService.getBrowsers().subscribe((browsers: any) => (this.paginationBrowsers = browsers.data));
        this.browserService.getBrowsers().subscribe((browsers: any) => (this.responsiveBrowsers = browsers.data));
        this.browserService.getBrowsers().subscribe((browsers: any) => (this.advancedBrowsers = browsers.data));
    }

    selectBrowser(browser: Browser) {
        this.selectedBrowser = browser;
        this.displayDialog = true;
    }

    onDialogHide() {
        this.selectedBrowser = null;
    }

    onPagination($event: any) {
        this.msgs.length = 0;
        this.msgs.push({
            severity: 'info',
            summary: 'The first record  is ' + $event.first + 'th one.',
            detail: 'There are " + $event.rows + " rows in this current page'
        });
    }

    loadData(event: any) {
        const start = event.first; // event.first = First row offset
        const end = start + event.rows; // event.rows = Number of rows per page
        this.browserService.getBrowsers().subscribe((browsers: any) => (this.lazyloadingBrowsers = browsers.data.slice(start, end)));
    }

    onChangeStep(label: string) {
        this.msgs.length = 0;
        this.msgs.push({ severity: 'info', summary: label });
    }
}
