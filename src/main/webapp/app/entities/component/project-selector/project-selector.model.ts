import * as dataModel from '../../../shared/model/project.model';

export class ProjectSelectorItem {
    public displayedValue?: string;
    public id?: number;
    constructor(public data?: dataModel.IProject) {
        this.id = this.data.id;
        if (data != null) {
            this.displayedValue = `${data.code} - ${data.description}`;
        } else {
            this.displayedValue = 'None';
        }
    }
}
