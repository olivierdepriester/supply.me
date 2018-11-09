import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'smSupplier' })
export class SmSupplierPipe implements PipeTransform {
    transform(value: any, args?: any): any {
        if (value == null || value === undefined) {
            return '-';
        } else {
            return `${value.referenceNumber} - ${value.name}`;
        }
    }
}
