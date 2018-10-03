import * as dataModel from '../../../shared/model/project.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';

export class ProjectSelectorItem extends AbstractSelectorItem {
    public displayValue: string;
    public description: string;
    constructor(data: dataModel.IProject) {
        super(data);
        this.displayValue = `${data.code} - ${data.description}`;
        this.description = data.description;
    }
}
