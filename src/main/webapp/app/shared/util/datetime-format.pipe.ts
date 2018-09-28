import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'datetimeFormat'
})
export class DatetimeFormatPipe extends DatePipe implements PipeTransform {
    transform(value: any, args?: any): any {
        return super.transform(value, 'yyyy-MM-dd HH:mm:ss', '+0800', 'en');
    }
}
