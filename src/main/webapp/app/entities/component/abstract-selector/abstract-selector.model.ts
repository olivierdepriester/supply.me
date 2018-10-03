import { ISelectable } from 'app/shared/model/selectable.model';

export abstract class AbstractSelectorItem {
    public id?: number;
    public data?: ISelectable;
    constructor(data: ISelectable) {
        this.data = data;
        if (this.data != null) {
            this.id = this.data.id;
        }
    }

    public abstract get displayValue(): string;

    public abstract get description(): string;
}
