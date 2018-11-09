import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'smDepartment' })
export class SmDepartmentPipe implements PipeTransform {
    transform(value: any, args?: any): any {
        if (value == null || value === undefined) {
            return '-';
        } else {
            return `${value.code} - ${value.description}`;
        }
    }
}
