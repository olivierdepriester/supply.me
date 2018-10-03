import * as dataModel from '../../../shared/model/supplier.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';

export class SupplierSelectorItem extends AbstractSelectorItem {
    public displayValue: string;
    public description: string;
    constructor(data: dataModel.ISupplier) {
        super(data);
        this.displayValue = `${data.referenceNumber} - ${data.name}`;
        this.description = data.name;
    }
}
