import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VCard } from './vcard';

@Injectable()
export class VCardService {
    constructor(private http: HttpClient) {}

    getVCards(): Observable<VCard[]> {
        return this.http.get<VCard[]>('content/primeng/assets/data/json/vcards/vcards.json');
    }
}
