import { HttpResponse } from '@angular/common/http';
import { Component, forwardRef, Input, OnInit, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { SELECTOR_SIZE } from 'app/app.constants';
import { ProjectService } from 'app/entities/project/project.service';
import { AutoComplete } from 'primeng/primeng';
import * as dataModel from '../../../shared/model/project.model';
import { ProjectSelectorItem } from './';

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
export class ProjectSelectorComponent implements OnInit, ControlValueAccessor {
    filteredItems: ProjectSelectorItem[];
    selectedItem: ProjectSelectorItem;
    @Input() selectedData: dataModel.IProject;

    @ViewChild(AutoComplete) private autoComplete: AutoComplete;
    private lastQuery = '';

    constructor(private service: ProjectService) {}

    validateForm(): boolean {
        return true;
    }

    ngOnInit() {}

    searchData(event) {
        this.lastQuery = event.query;
        this.search();
    }

    private search() {
        // Search project
        this.service
            .search({ query: this.lastQuery, size: SELECTOR_SIZE, sort: ['code.keyword,asc', 'id'] })
            .subscribe((res: HttpResponse<dataModel.IProject[]>) => {
                this.filteredItems = Array.from(res.body, data => new ProjectSelectorItem(data));
            });
    }

    onSelect(event) {
        this.value = event.data;
    }

    onFocus(event: any) {
        if (this.filteredItems == null && this.lastQuery === '') {
            // If no search before : focus send a search based on an empty query
            this.search();
        } else {
            // Otherwise refresh the list content with the latest results
            this.filteredItems = Array.from(this.filteredItems);
        }
        this.autoComplete.show();
    }

    onClear() {
        this.value = null;
    }

    get value(): any {
        return this.selectedData;
    }

    // set accessor including call the onchange callback
    set value(v: any) {
        if (v !== this.selectedData) {
            this.selectedData = v;
            this.onChange(v);
        }
    }

    /**
     * Invoked when the model has been changed
     */
    onChange: (_: any) => void = (_: any) => {};

    /**
     * Invoked when the model has been touched
     */
    onTouched: () => void = () => {};

    writeValue(value: any): void {
        if (value !== undefined && value != null) {
            this.value = value;
            this.selectedItem = new ProjectSelectorItem(value);
        } else {
            this.value = null;
            this.selectedItem = null;
        }
    }

    /**
     * Registers a callback function that should be called when the control's value changes in the UI.
     * @param fn
     */
    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    /**
     * Registers a callback function that should be called when the control receives a blur event.
     * @param fn
     */
    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    setDisabledState?(isDisabled: boolean): void {}
}
