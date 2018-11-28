import * as dataModel from '../../../shared/model/demand.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';

export class DemandSelectorItem extends AbstractSelectorItem {
    public description: string;
    public displayValue: string;
    constructor(public data?: dataModel.IDemand) {
        super(data);
        this.displayValue = `${data.code} - ${data.material.name} [${data.department.code}]`;
        this.description = `${data.quantity} x [${data.material.partNumber} - ${data.material.name}]
        \n${data.department.code} - ${data.department.description}
        \n${data.project.code} - ${data.project.description}`;
    }
}
