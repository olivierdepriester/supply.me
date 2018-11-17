import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import Browser from './browser';

@Injectable()
export class BrowserService {
    constructor(private http: HttpClient) {}

    getBrowsers(): Observable<Browser[]> {
        return this.http.get<Browser[]>('content/primeng/assets/data/json/browsers/browsers.json');
    }
}
