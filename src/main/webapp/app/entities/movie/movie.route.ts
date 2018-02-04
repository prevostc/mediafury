import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MovieComponent } from './movie.component';
import { MovieDetailComponent } from './movie-detail.component';
import { MoviePopupComponent } from './movie-dialog.component';
import { MovieDeletePopupComponent } from './movie-delete-dialog.component';

@Injectable()
export class MovieResolvePagingParams implements Resolve<any> {

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

export const movieRoute: Routes = [
    {
        path: 'movie',
        component: MovieComponent,
        resolve: {
            'pagingParams': MovieResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'movie/:id',
        component: MovieDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const moviePopupRoute: Routes = [
    {
        path: 'movie-new',
        component: MoviePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'movie/:id/edit',
        component: MoviePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'movie/:id/delete',
        component: MovieDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
