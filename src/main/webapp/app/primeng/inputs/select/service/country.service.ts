import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import Country from './country';

@Injectable()
export class CountryService {
    constructor(private http: HttpClient) {}

    getCountries(): Observable<Country[]> {
        return this.http.get<Country[]>('content/primeng/assets/data/json/countries/countries.json');
    }
}
