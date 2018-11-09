import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'smProject' })
export class SmProjectPipe implements PipeTransform {
    transform(value: any, args?: any): any {
        if (value == null || value === undefined) {
            return '-';
        } else {
            return `${value.code} - ${value.description}`;
        }
    }
}
