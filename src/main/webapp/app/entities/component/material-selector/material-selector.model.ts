import * as dataModel from '../../../shared/model/material.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';

export class MaterialSelectorItem extends AbstractSelectorItem {
    public displayValue: string;
    public description: string;
    constructor(public data?: dataModel.IMaterial) {
        super(data);
        this.displayValue = `${data.partNumber} - ${data.name}`;
        this.description = data.description;
    }
}
