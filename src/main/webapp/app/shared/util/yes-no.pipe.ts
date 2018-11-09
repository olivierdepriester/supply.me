import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'yesNo'
})
export class YesNoPipe implements PipeTransform {
    transform(value: boolean, args?: any): any {
        if (value == null || value === undefined) {
            return null;
        } else {
            return `global.field.${value ? 'yes' : 'no'}`;
        }
    }
}
