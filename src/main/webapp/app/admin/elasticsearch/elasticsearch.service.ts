import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IElasticsearchIndexResult } from './elasticsearch.model';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class ElasticsearchService {
    public resourceUrl = SERVER_API_URL + 'management/indices';

    constructor(private http: HttpClient) {}

    findAll(): Observable<HttpResponse<IElasticsearchIndexResult[]>> {
        return this.http.get<IElasticsearchIndexResult[]>(this.resourceUrl, { observe: 'response' });
    }

    rebuildIndex(index: IElasticsearchIndexResult): Observable<HttpResponse<IElasticsearchIndexResult>> {
        return this.http.post<IElasticsearchIndexResult>(`${this.resourceUrl}/${index.className}/rebuild/`, null, { observe: 'response' });
    }
}
