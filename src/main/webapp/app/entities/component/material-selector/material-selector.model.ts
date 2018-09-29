import * as dataModel from '../../../shared/model/material.model';

export class MaterialSelectorItem {
    public displayedValue?: string;
    public id?: number;
    constructor(public data?: dataModel.IMaterial) {
        this.id = this.data.id;
        this.displayedValue = `${data.partNumber} - ${data.name}`;
    }
}
