import { Directive } from '@angular/core';
import { Validator, NG_VALIDATORS, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

@Directive({
    selector: '[jhiRequired]',
    providers: [{ provide: NG_VALIDATORS, useExisting: SelectorRequiredDirective, multi: true }]
})
export class SelectorRequiredDirective implements Validator {
    validate(control: AbstractControl): ValidationErrors {
        if (control.value != null) {
            return null;
        } else {
            return { required: 'Please fill the field' };
        }
    }

    constructor() {}
}
