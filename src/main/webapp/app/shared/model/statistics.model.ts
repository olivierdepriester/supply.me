export interface IDataPoint {
    name: any;
    value: number;
    minValue?: number;
    maxValue?: number;
}

export interface ISerie {
    key?: any;
    dataPoints?: IDataPoint[];
}

export interface IStatistics {
    /**
     * Series to display on the chart
     *
     * @type {ISerie[]}
     * @memberof IStatistics
     */
    series?: ISerie[];
}

export class DataPoint implements IDataPoint {
    /**
     *
     */
    constructor(public name: any, public value: number, public minValue?: number, public maxValue?: number) {}
}

/**
 * Serie of statistics for a chart.
 *
 * @export
 * @class Serie
 * @implements {ISerie}
 */
export class Serie implements ISerie {
    constructor(public key?: any, public dataPoints?: IDataPoint[]) {}
}

/**
 * Data to display on a chart
 *
 * @export
 * @class Statistics
 * @implements {IStatistics}
 */
export class Statistics implements IStatistics {
    constructor(public series?: ISerie[]) {}
}
