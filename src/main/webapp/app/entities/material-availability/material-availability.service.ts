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
    public resourceUrl = SERVER_API_URL + 'api/material-availabilities';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/material-availabilities';

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

    findForMaterial(materialId: number): Observable<EntityArrayResponseType> {
        return this.http
            .get<IMaterialAvailability[]>(`${this.resourceUrl}/material/${materialId}`, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

    protected convertDateFromClient(materialAvailability: IMaterialAvailability): IMaterialAvailability {
        const copy: IMaterialAvailability = Object.assign({}, materialAvailability, {
            creationDate:
                materialAvailability.creationDate != null && materialAvailability.creationDate.isValid()
                    ? materialAvailability.creationDate.toJSON()
                    : null,
            updateDate:
                materialAvailability.updateDate != null && materialAvailability.updateDate.isValid()
                    ? materialAvailability.updateDate.toJSON()
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
            res.body.updateDate = res.body.updateDate != null ? moment(res.body.updateDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((materialAvailability: IMaterialAvailability) => {
                materialAvailability.creationDate =
                    materialAvailability.creationDate != null ? moment(materialAvailability.creationDate) : null;
                materialAvailability.updateDate = materialAvailability.updateDate != null ? moment(materialAvailability.updateDate) : null;
            });
        }
        return res;
    }
}
