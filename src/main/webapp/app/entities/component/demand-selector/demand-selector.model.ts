import * as demandModel from '../../../shared/model/demand.model';

export class DemandSelectorItem {
    public displayedValue?: string;
    public id?: number;
    constructor(public demand?: demandModel.IDemand) {
        this.id = this.demand.id;
        this.displayedValue = `${demand.material.partNumber} - ${demand.material.name} [${demand.project.code}]`;
    }
}
