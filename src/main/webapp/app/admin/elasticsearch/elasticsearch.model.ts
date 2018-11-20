export interface IElasticsearchIndexResult {
    className: string;
    count: number;
    entityId?: number;
    isRunning?: boolean;
}

export class ElasticsearchIndexResult implements IElasticsearchIndexResult {
    /**
     *
     */
    constructor(public className: string, public count: number, public entityId?: number, public isRunning = false) {}
}
