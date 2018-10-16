import { HttpResponse } from '@angular/common/http';
import { Input, OnInit } from '@angular/core';
import { ControlValueAccessor } from '@angular/forms';
import { ISelectable } from 'app/shared/model/selectable.model';
import { Observable } from 'rxjs';
import { AbstractSelectorItem } from './abstract-selector.model';

export abstract class AbstractSelectorComponent<T extends ISelectable, K extends AbstractSelectorItem>
    implements OnInit, ControlValueAccessor {
    filteredItems: K[];
    selectedItem: K;
    /**
     * Set or get if the autocomplete has to be refreshed
     *
     * @protected
     * @memberof AbstractSelectorComponent
     */
    protected forceRefresh = false;
    private lastQuery = '';

    @Input() selectedData: T;

    constructor() {}

    ngOnInit() {}

    searchData(event) {
        this.lastQuery = event.query;
        this.search();
    }

    protected abstract getNew(data: T): K;

    protected abstract searchServiceFunction(myQuery: string): Observable<HttpResponse<T[]>>;

    private search() {
        this.searchServiceFunction(this.lastQuery).subscribe((res: HttpResponse<T[]>) => {
            this.filteredItems = Array.from(res.body, data => this.getNew(data));
        });
        this.forceRefresh = false;
    }

    onSelect(event) {
        this.value = event.data;
    }

    onFocus() {
        if (this.itemListNeedRefresh()) {
            // If no search before : focus send a search based on an empty query
            this.search();
        } else {
            // Otherwise refresh the list content with the latest results
            this.filteredItems = Array.from(this.filteredItems);
        }
        this.getAutoCompleteComponent().show();
    }

    onClear() {
        this.value = null;
    }

    private itemListNeedRefresh(): boolean {
        return this.forceRefresh || (this.filteredItems == null && this.lastQuery === '');
    }

    protected abstract getAutoCompleteComponent();

    public get value(): any {
        return this.selectedData;
    }

    // set accessor including call the onchange callback
    public set value(v: any) {
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
            this.selectedItem = this.getNew(value);
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
