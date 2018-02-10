import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Movie } from './movie.model';
import { MovieService } from './movie.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {Category, CategoryService} from '../category';

@Component({
    selector: 'jhi-movie',
    templateUrl: './movie.component.html'
})
export class MovieComponent implements OnInit, OnDestroy {

currentAccount: any;
    movies: Movie[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    categories: Category[] = [];
    selectedCategory: Category;

    constructor(
        private movieService: MovieService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private categoryService: CategoryService,
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.movies = [];
        const params = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };

        // @todo: avoid this leaking abstraction
        if (this.selectedCategory) {
            params['categoryId.equals'] = this.selectedCategory.id;
        }

        this.movieService.query(params).subscribe(
                (res: HttpResponse<Movie[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/movie'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/movie', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInMovies();

        this.categoryService.query()
            .subscribe(
                (res: HttpResponse<Category[]>) => { this.categories = res.body; },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Movie) {
        return item.id;
    }
    registerChangeInMovies() {
        this.eventSubscriber = this.eventManager.subscribe('movieListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    onCategoryChange(category: Category) {
        this.selectedCategory = category;
        this.loadAll();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.movies = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
