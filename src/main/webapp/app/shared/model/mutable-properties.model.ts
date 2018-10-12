export const enum PropertiesKey {
    THRESHOLD_SECOND_LEVEL_APPROVAL = 'THRESHOLD_SECOND_LEVEL_APPROVAL',
    PURCHASE_ORDER_CODE_GENERATOR = 'PURCHASE_ORDER_CODE_GENERATOR',
    MATERIAL_PART_NUMBER_GENERATOR = 'MATERIAL_PART_NUMBER_GENERATOR'
}

export interface IMutableProperties {
    id?: number;
    key?: PropertiesKey;
    value?: string;
    valueType?: string;
}

export class MutableProperties implements IMutableProperties {
    constructor(public id?: number, public key?: PropertiesKey, public value?: string, public valueType?: string) {}
}
