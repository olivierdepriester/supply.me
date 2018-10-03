import { HttpResponse } from '@angular/common/http';
import { Component, forwardRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { SELECTOR_SIZE } from 'app/app.constants';
import { ProjectService } from 'app/entities/project/project.service';
import { AutoComplete } from 'primeng/primeng';
import * as dataModel from '../../../shared/model/project.model';
import { ProjectSelectorItem } from './';
import { AbstractSelectorComponent } from 'app/entities/component/abstract-selector';
import { Observable } from 'rxjs';

@Component({
    selector: 'jhi-project-selector',
    templateUrl: './project-selector.component.html',
    styles: [],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => ProjectSelectorComponent),
            multi: true
        }
    ]
})
export class ProjectSelectorComponent extends AbstractSelectorComponent<dataModel.IProject, ProjectSelectorItem> {
    @ViewChild(AutoComplete) private autoComplete: AutoComplete;

    protected getNew(data: dataModel.IProject): ProjectSelectorItem {
        return new ProjectSelectorItem(data);
    }
    protected searchServiceFunction(textQuery: string): Observable<HttpResponse<dataModel.IProject[]>> {
        return this.service.search({ query: textQuery, size: SELECTOR_SIZE, sort: ['code.keyword,asc', 'id'] });
    }
    protected getAutoCompleteComponent() {
        return this.autoComplete;
    }

    constructor(private service: ProjectService) {
        super();
    }
}
