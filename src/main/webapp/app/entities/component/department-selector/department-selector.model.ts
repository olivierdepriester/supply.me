import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';
import * as dataModel from '../../../shared/model/department.model';

export class DepartmentSelectorItem extends AbstractSelectorItem {
    public description: string;
    public displayValue: string;
    constructor(public data?: dataModel.IDepartment) {
        super(data);
        this.displayValue = `${data.code} - ${data.description}`;
        this.description = this.displayValue;
    }
}
