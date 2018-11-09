import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'smUser' })
export class SmUserPipe implements PipeTransform {
    transform(value: any, args?: any): any {
        if (value == null || value === undefined) {
            return '-';
        } else {
            return `${value.lastName} ${value.firstName}`;
        }
    }
}
