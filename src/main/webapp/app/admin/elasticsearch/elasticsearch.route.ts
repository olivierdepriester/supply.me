import { Route } from '@angular/router';
import { ElasticsearchComponent } from './elasticsearch.component';

export const elasticsearchRoute: Route = {
    path: 'elasticsearch',
    component: ElasticsearchComponent,
    data: {
        pageTitle: 'elasticsearch.title'
    }
};
