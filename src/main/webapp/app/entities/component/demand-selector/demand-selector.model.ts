import * as dataModel from '../../../shared/model/demand.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';

export class DemandSelectorItem extends AbstractSelectorItem {
    public description: string;
    public displayValue: string;
    constructor(public data?: dataModel.IDemand) {
        super(data);
        this.displayValue = `${data.material.partNumber} - ${data.material.name} [${data.project.code}]`;
        this.description = `${data.material.partNumber} - ${data.material.name} [${data.quantity}]\n${data.project.code} - ${
            data.project.description
        }`;
    }
}
