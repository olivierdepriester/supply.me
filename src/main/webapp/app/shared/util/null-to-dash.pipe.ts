import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'nullToDash' })
export class NullToDashPipe implements PipeTransform {
    transform(value: any, args?: any): any {
        if (value === null || value === undefined || (value as string).trim() === '') {
            return '-';
        } else {
            return value;
        }
    }
}
