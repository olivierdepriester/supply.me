import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import Browser from './browser';

@Injectable()
export class BrowserService {
    constructor(private http: HttpClient) {}

    getBrowsers(): Observable<Browser[]> {
        return this.http.get<Browser[]>('/assets/data/json/browsers/browsers.json');
    }
}
