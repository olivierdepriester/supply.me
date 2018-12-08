import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Observable } from 'rxjs';
import { IStatistics } from '../../shared/model/statistics.model';

type EntityResponseType = HttpResponse<IStatistics>;

@Injectable({ providedIn: 'root' })
export class StatisticsService {
    public resourceUrl = SERVER_API_URL + 'api/statistics';

    constructor(private http: HttpClient) {}

    getPriceEvolutionForMaterial(id: number): Observable<EntityResponseType> {
        return this.http.get<IStatistics>(`${this.resourceUrl}/material/${id}/price-per-month/`, { observe: 'response' });
    }

    getQuantityOrderedForMaterial(id: number): Observable<EntityResponseType> {
        return this.http.get<IStatistics>(`${this.resourceUrl}/material/${id}/quantity-per-month/`, { observe: 'response' });
    }
}
