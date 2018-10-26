import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL, SORTED_AUTHORITIES } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { DemandStatus, IDemand } from 'app/shared/model/demand.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Principal } from 'app/core';

type EntityResponseType = HttpResponse<IDemand>;
type EntityArrayResponseType = HttpResponse<IDemand[]>;

@Injectable({ providedIn: 'root' })
export class DemandService {
    private resourceUrl = SERVER_API_URL + 'api/demands';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/demands';

    constructor(private http: HttpClient, private principal: Principal) {}

    create(demand: IDemand): Observable<EntityResponseType> {
        demand.creationDate = moment();
        demand.status = DemandStatus.NEW;
        const copy = this.convertDateFromClient(demand);
        return this.http
            .post<IDemand>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(demand: IDemand): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(demand);
        return this.http
            .put<IDemand>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    changeStatus(id: number, status: DemandStatus, statusComment: string) {
        const options = { id: `${id}`, status: `${status}`, comment: statusComment };
        return this.http
            .put<IDemand>(`${this.resourceUrl}/changeStatus`, options, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    searchPurchasable(req?: any) {
        const options = createRequestOption(req);
        return this.http
            .get<IDemand[]>(`${this.resourceSearchUrl}/purchasable`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    find(id: number, loadChanges?: boolean): Observable<EntityResponseType> {
        let options: HttpParams;
        if (loadChanges != null) {
            options = new HttpParams().append('loadChanges', `${loadChanges}`);
        }
        return this.http
            .get<IDemand>(`${this.resourceUrl}/${id}`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDemand[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        let options = createRequestOption(req.query);
        if (req.sort) {
            req.sort.forEach(val => {
                options = options.append('sort', val);
            });
        }
        return this.http
            .get<IDemand[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(demand: IDemand): IDemand {
        const copy: IDemand = Object.assign({}, demand, {
            expectedDate: demand.expectedDate != null && demand.expectedDate.isValid() ? demand.expectedDate.toJSON() : null,
            creationDate: demand.creationDate != null && demand.creationDate.isValid() ? demand.creationDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expectedDate = res.body.expectedDate != null ? moment(res.body.expectedDate) : null;
        res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((demand: IDemand) => {
            demand.expectedDate = demand.expectedDate != null ? moment(demand.expectedDate) : null;
            demand.creationDate = demand.creationDate != null ? moment(demand.creationDate) : null;
        });
        return res;
    }

    isEditAllowed(demand: IDemand, account: any): boolean {
        const result =
            (demand.status === DemandStatus.NEW || demand.status === DemandStatus.REJECTED) &&
            (this.principal.hasAnyAuthorityDirect(['ROLE_ADMIN']) || demand.creationUser.id === account.id);
        return result;
    }

    isDeleteAllowed(demand: IDemand, account: any): boolean {
        const result =
            demand.status !== DemandStatus.FULLY_DELIVERED &&
            demand.status !== DemandStatus.PARTIALLY_DELIVERED &&
            demand.status !== DemandStatus.ORDERED &&
            (this.principal.hasAnyAuthorityDirect(['ROLE_ADMIN']) || demand.creationUser.id === account.id);
        return result;
    }

    isApprovalAllowed(demand: IDemand, account: any): boolean {
        const result =
            demand.status === DemandStatus.WAITING_FOR_APPROVAL &&
            this.principal.getCurrentUserHighestValidationAuthority() != null &&
            ((demand.reachedAuthority == null && demand.project.headUser && demand.project.headUser.id === account.id) ||
                (demand.reachedAuthority !== this.principal.getCurrentUserHighestValidationAuthority() &&
                    this.principal.getCurrentUserHighestValidationAuthority() ===
                        this.principal.getHigherAuthority(
                            demand.reachedAuthority,
                            this.principal.getCurrentUserHighestValidationAuthority()
                        )));
        return result;
    }

    isRejectAllowed(demand: IDemand, account: any): boolean {
        return (
            demand.status === DemandStatus.WAITING_FOR_APPROVAL &&
            account.authorities &&
            account.authorities.includes('ROLE_VALIDATOR_LVL1')
        );
    }
}
