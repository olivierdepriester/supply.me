import { Component, OnInit } from '@angular/core';
import { ElasticsearchService } from './elasticsearch.service';
import { IElasticsearchIndexResult } from './elasticsearch.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

export const PARALLELISM_LEVEL = 3; // Number of parallel reindex requests
export const REBUILD_ALL_MAX_WAIT = 5; // In seconds
@Component({
    selector: 'jhi-elasticsearch',
    templateUrl: './elasticsearch.component.html',
    styles: []
})
export class ElasticsearchComponent implements OnInit {
    indices: IElasticsearchIndexResult[] = [];
    isRebuildingAll = false;
    reindexedCount = 0;
    progressText: string;

    constructor(private elasticsearchService: ElasticsearchService) {}

    ngOnInit() {
        this.elasticsearchService.findAll().subscribe(res => (this.indices = res.body));
    }

    rebuild(index: IElasticsearchIndexResult): void {
        index.isRunning = true;
        this.elasticsearchService.rebuildIndex(index).subscribe(
            (res: HttpResponse<IElasticsearchIndexResult>) => {
                index.count = res.body.count;
                index.isRunning = false;
            },
            (res: HttpErrorResponse) => (index.isRunning = false)
        );
    }

    rebuildAll(): void {
        this.isRebuildingAll = true;
        this.reindexedCount = 0;
        this.indices.forEach(element => {
            element.count = null;
            element.isRunning = false;
        });
        for (let index = 0; index < PARALLELISM_LEVEL; index++) {
            // Launch 1 rebuild "thread"
            this.recursiveRebuild(this.indices[index]);
        }
        this.recursiveCheck(0);
    }

    private recursiveCheck(iterationNumber: number) {
        setTimeout(() => {
            // Check if it still exists an index in rebuild running state
            if (this.indices.find(i => i.isRunning) === undefined) {
                this.isRebuildingAll = false;
            }

            if (iterationNumber++ === REBUILD_ALL_MAX_WAIT) {
                this.isRebuildingAll = false;
            }
            if (this.isRebuildingAll) {
                // Wait 1 second more
                this.recursiveCheck(iterationNumber);
            }
        }, 1000);
    }

    /**
     * Rebuild 1 index and launch the rebuild of the next available index
     *
     * @private
     * @param {IElasticsearchIndexResult} index Index to rebuild
     * @memberof ElasticsearchComponent
     */
    private recursiveRebuild(index: IElasticsearchIndexResult) {
        // Mark the index rebuild as running
        index.isRunning = true;
        // Launch the rebuild
        this.elasticsearchService.rebuildIndex(index).subscribe(
            (res: HttpResponse<IElasticsearchIndexResult>) => {
                // Update displayed data
                index.count = res.body.count;
                this.onEndReindex(index);
                // Check if another index needs to be rebuilt
                const newIndex = this.indices.find(i => !i.isRunning && i.count == null);
                if (newIndex) {
                    // Launch the next index rebuild
                    this.recursiveRebuild(newIndex);
                }
            },
            (res: HttpErrorResponse) => this.onEndReindex(index)
        );
    }

    private onEndReindex(index: IElasticsearchIndexResult) {
        index.isRunning = false;
        this.reindexedCount++;
        this.progressText = `${this.reindexedCount} / ${this.indices.length}`;
    }
}
