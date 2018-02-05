import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { PersonComponent } from './person.component';
import { PersonDetailComponent } from './person-detail.component';
import { PersonPopupComponent } from './person-dialog.component';
import { PersonDeletePopupComponent } from './person-delete-dialog.component';

@Injectable()
export class PersonResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const personRoute: Routes = [
    {
        path: 'person',
        component: PersonComponent,
        resolve: {
            'pagingParams': PersonResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.person.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'person/:id',
        component: PersonDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.person.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const personPopupRoute: Routes = [
    {
        path: 'person-new',
        component: PersonPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.person.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'person/:id/edit',
        component: PersonPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.person.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'person/:id/delete',
        component: PersonDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.person.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
