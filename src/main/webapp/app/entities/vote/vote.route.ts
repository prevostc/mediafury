import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { VoteComponent } from './vote.component';
import { VoteDetailComponent } from './vote-detail.component';
import { VotePopupComponent } from './vote-dialog.component';
import { VoteDeletePopupComponent } from './vote-delete-dialog.component';

@Injectable()
export class VoteResolvePagingParams implements Resolve<any> {

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

export const voteRoute: Routes = [
    {
        path: 'vote',
        component: VoteComponent,
        resolve: {
            'pagingParams': VoteResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vote/:id',
        component: VoteDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const votePopupRoute: Routes = [
    {
        path: 'vote-new',
        component: VotePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vote/:id/edit',
        component: VotePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vote/:id/delete',
        component: VoteDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'mediafuryApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
