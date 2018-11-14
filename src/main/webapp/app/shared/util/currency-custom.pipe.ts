import { CurrencyPipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'currencyCustom' })
export class CurrencyCustomPipe extends CurrencyPipe implements PipeTransform {
    transform(value: any, args?: any): any {
        return super.transform(value, 'CNY', 'symbol-narrow', '1.2-3', 'en');
    }
}
