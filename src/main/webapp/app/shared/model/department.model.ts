import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IProject } from 'app/shared/model//project.model';

export interface IDepartment {
    id?: number;
    code?: string;
    description?: string;
    creationDate?: Moment;
    active?: boolean;
    creationUser?: IUser;
    headUser?: IUser;
    defaultProject?: IProject;
}

export class Department implements IDepartment {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public creationDate?: Moment,
        public active?: boolean,
        public creationUser?: IUser,
        public headUser?: IUser,
        public defaultProject?: IProject
    ) {
        this.active = this.active || false;
    }
}
