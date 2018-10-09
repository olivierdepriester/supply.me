import { Component, forwardRef, ViewChild } from '@angular/core';
import { AbstractSelectorComponent } from '../abstract-selector';
import * as dataModel from '../../../core/user/user.model';
import { UserSelectorItem } from './user-selector.model';
import { UserService } from '../../../core/user/user.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { SELECTOR_SIZE } from 'app/app.constants';
import { AutoComplete } from 'primeng/primeng';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
    selector: 'jhi-user-selector',
    templateUrl: './user-selector.component.html',
    styles: [],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => UserSelectorComponent),
            multi: true
        }
    ]
})
export class UserSelectorComponent extends AbstractSelectorComponent<dataModel.IUser, UserSelectorItem> {
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;

    protected searchServiceFunction(textQuery: string): Observable<HttpResponse<dataModel.IUser[]>> {
        return this.service.search({
            query: textQuery,
            size: SELECTOR_SIZE,
            sort: ['lastName.keyword,asc', 'firstName.keyword,asc', 'id']
        });
    }
    protected getNew(data: dataModel.IUser): UserSelectorItem {
        return new UserSelectorItem(data);
    }

    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }

    constructor(private service: UserService) {
        super();
    }
}
