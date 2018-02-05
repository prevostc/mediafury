import { Route } from '@angular/router';

import { FuryComponent } from './';

export const FURY_ROUTE: Route = {
    path: '',
    component: FuryComponent,
    data: {
        authorities: [],
        pageTitle: 'about.title'
    }
};
