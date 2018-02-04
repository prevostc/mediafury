import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MoviePerson } from './movie-person.model';
import { MoviePersonPopupService } from './movie-person-popup.service';
import { MoviePersonService } from './movie-person.service';
import { Movie, MovieService } from '../movie';
import { Person, PersonService } from '../person';

@Component({
    selector: 'jhi-movie-person-dialog',
    templateUrl: './movie-person-dialog.component.html'
})
export class MoviePersonDialogComponent implements OnInit {

    moviePerson: MoviePerson;
    isSaving: boolean;

    movies: Movie[];

    people: Person[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private moviePersonService: MoviePersonService,
        private movieService: MovieService,
        private personService: PersonService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.movieService.query()
            .subscribe((res: HttpResponse<Movie[]>) => { this.movies = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.personService.query()
            .subscribe((res: HttpResponse<Person[]>) => { this.people = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.moviePerson.id !== undefined) {
            this.subscribeToSaveResponse(
                this.moviePersonService.update(this.moviePerson));
        } else {
            this.subscribeToSaveResponse(
                this.moviePersonService.create(this.moviePerson));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MoviePerson>>) {
        result.subscribe((res: HttpResponse<MoviePerson>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: MoviePerson) {
        this.eventManager.broadcast({ name: 'moviePersonListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackMovieById(index: number, item: Movie) {
        return item.id;
    }

    trackPersonById(index: number, item: Person) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-movie-person-popup',
    template: ''
})
export class MoviePersonPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private moviePersonPopupService: MoviePersonPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.moviePersonPopupService
                    .open(MoviePersonDialogComponent as Component, params['id']);
            } else {
                this.moviePersonPopupService
                    .open(MoviePersonDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
