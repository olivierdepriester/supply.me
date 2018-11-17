import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class TreeNodeService {
    constructor(private http: HttpClient) {}

    getTouristPlaces(): Observable<any[]> {
        return this.http.get<any[]>('content/primeng/assets/data/json/cities/cities.json');
    }
}
