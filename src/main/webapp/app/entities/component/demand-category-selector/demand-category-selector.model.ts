import * as dataModel from '../../../shared/model/demand-category.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';
import { NullToDashPipe } from 'app/shared';

export class DemandCategorySelectorItem extends AbstractSelectorItem {
    public description: string;
    public displayValue: string;
    constructor(public data?: dataModel.IDemandCategory) {
        super(data);
        this.displayValue = `${data.name}`;
        this.description = `${data.name}\n${new NullToDashPipe().transform(data.description)}`;
    }
}
