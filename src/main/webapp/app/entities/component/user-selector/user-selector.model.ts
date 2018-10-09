import * as dataModel from '../../../core/user/user.model';
import { AbstractSelectorItem } from 'app/entities/component/abstract-selector';

export class UserSelectorItem extends AbstractSelectorItem {
    public displayValue: string;
    public description: string;
    constructor(data: dataModel.IUser) {
        super(data);
        this.displayValue = `${data.lastName} ${data.firstName}`;
        this.description = data.email;
    }
}
