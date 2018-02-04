import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MoviePersonComponent } from './movie-person.component';
import { MoviePersonDetailComponent } from './movie-person-detail.component';
import { MoviePersonPopupComponent } from './movie-person-dialog.component';
import { MoviePersonDeletePopupComponent } from './movie-person-delete-dialog.component';

@Injectable()
export class MoviePersonResolvePagingParams implements Resolve<any> {

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

export const moviePersonRoute: Routes = [
    {
        path: 'movie-person',
        component: MoviePersonComponent,
        resolve: {
            'pagingParams': MoviePersonResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.moviePerson.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'movie-person/:id',
        component: MoviePersonDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.moviePerson.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const moviePersonPopupRoute: Routes = [
    {
        path: 'movie-person-new',
        component: MoviePersonPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.moviePerson.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'movie-person/:id/edit',
        component: MoviePersonPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.moviePerson.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'movie-person/:id/delete',
        component: MoviePersonDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mediafuryApp.moviePerson.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
