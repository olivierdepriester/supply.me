import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialAvailability } from 'app/shared/model/material-availability.model';

type EntityResponseType = HttpResponse<IMaterialAvailability>;
type EntityArrayResponseType = HttpResponse<IMaterialAvailability[]>;

@Injectable({ providedIn: 'root' })
export class MaterialAvailabilityService {
    private resourceUrl = SERVER_API_URL + 'api/material-availabilities';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/material-availabilities';

    constructor(private http: HttpClient) {}

    create(materialAvailability: IMaterialAvailability): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(materialAvailability);
        return this.http
            .post<IMaterialAvailability>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(materialAvailability: IMaterialAvailability): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(materialAvailability);
        return this.http
            .put<IMaterialAvailability>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMaterialAvailability>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMaterialAvailability[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMaterialAvailability[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(materialAvailability: IMaterialAvailability): IMaterialAvailability {
        const copy: IMaterialAvailability = Object.assign({}, materialAvailability, {
            startDate:
                materialAvailability.startDate != null && materialAvailability.startDate.isValid()
                    ? materialAvailability.startDate.toJSON()
                    : null,
            endDate:
                materialAvailability.endDate != null && materialAvailability.endDate.isValid()
                    ? materialAvailability.endDate.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
        res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((materialAvailability: IMaterialAvailability) => {
            materialAvailability.startDate = materialAvailability.startDate != null ? moment(materialAvailability.startDate) : null;
            materialAvailability.endDate = materialAvailability.endDate != null ? moment(materialAvailability.endDate) : null;
        });
        return res;
    }
}
