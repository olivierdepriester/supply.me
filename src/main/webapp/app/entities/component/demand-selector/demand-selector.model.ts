import * as dataModel from '../../../shared/model/demand.model';

export class DemandSelectorItem {
    public displayedValue?: string;
    public id?: number;
    constructor(public data?: dataModel.IDemand) {
        this.id = this.data.id;
        this.displayedValue = `${data.material.partNumber} - ${data.material.name} [${data.project.code}]`;
    }
}
