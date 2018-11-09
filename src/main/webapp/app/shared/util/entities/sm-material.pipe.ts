import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'smMaterial' })
export class SmMaterialPipe implements PipeTransform {
    transform(value: any, args?: any): any {
        if (value == null || value === undefined) {
            return '-';
        } else {
            return `${value.partNumber} - ${value.name}`;
        }
    }
}
