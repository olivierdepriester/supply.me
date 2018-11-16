import { Moment } from 'moment';
import { IMaterialCategory } from 'app/shared/model//material-category.model';
import { IUser } from 'app/core/user/user.model';

export interface IMaterialCategory {
    id?: number;
    name?: string;
    description?: string;
    creationDate?: Moment;
    parentMaterialCategory?: IMaterialCategory;
    creationUser?: IUser;
}

export class MaterialCategory implements IMaterialCategory {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public creationDate?: Moment,
        public parentMaterialCategory?: IMaterialCategory,
        public creationUser?: IUser
    ) {}
}
