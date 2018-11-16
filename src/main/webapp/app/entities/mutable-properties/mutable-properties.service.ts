import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMutableProperties } from 'app/shared/model/mutable-properties.model';

type EntityResponseType = HttpResponse<IMutableProperties>;
type EntityArrayResponseType = HttpResponse<IMutableProperties[]>;

@Injectable({ providedIn: 'root' })
export class MutablePropertiesService {
    public resourceUrl = SERVER_API_URL + 'api/mutable-properties';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/mutable-properties';

    constructor(private http: HttpClient) {}

    create(mutableProperties: IMutableProperties): Observable<EntityResponseType> {
        return this.http.post<IMutableProperties>(this.resourceUrl, mutableProperties, { observe: 'response' });
    }

    update(mutableProperties: IMutableProperties): Observable<EntityResponseType> {
        return this.http.put<IMutableProperties>(this.resourceUrl, mutableProperties, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMutableProperties>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMutableProperties[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMutableProperties[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
