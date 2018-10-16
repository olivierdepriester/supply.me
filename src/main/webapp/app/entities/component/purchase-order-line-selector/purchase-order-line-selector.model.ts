import * as dataModel from '../../../shared/model/purchase-order-line.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';

export class PurchaseOrderLineSelectorItem extends AbstractSelectorItem {
    public displayValue: string;
    public description: string;
    constructor(data: dataModel.IPurchaseOrderLine) {
        super(data);
        this.displayValue = `${data.purchaseOrder.code} - ${data.demand.project.code} : ${data.quantity} x [${
            data.demand.material.partNumber
        } - ${data.demand.material.name}]`;
        this.description = data.demand.material.description;
    }
}
