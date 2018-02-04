import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MoviePerson } from './movie-person.model';
import { MoviePersonService } from './movie-person.service';

@Component({
    selector: 'jhi-movie-person-detail',
    templateUrl: './movie-person-detail.component.html'
})
export class MoviePersonDetailComponent implements OnInit, OnDestroy {

    moviePerson: MoviePerson;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private moviePersonService: MoviePersonService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMoviePeople();
    }

    load(id) {
        this.moviePersonService.find(id)
            .subscribe((moviePersonResponse: HttpResponse<MoviePerson>) => {
                this.moviePerson = moviePersonResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMoviePeople() {
        this.eventSubscriber = this.eventManager.subscribe(
            'moviePersonListModification',
            (response) => this.load(this.moviePerson.id)
        );
    }
}
