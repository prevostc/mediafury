import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Vote } from './vote.model';
import { VotePopupService } from './vote-popup.service';
import { VoteService } from './vote.service';
import { Movie, MovieService } from '../movie';

@Component({
    selector: 'jhi-vote-dialog',
    templateUrl: './vote-dialog.component.html'
})
export class VoteDialogComponent implements OnInit {

    vote: Vote;
    isSaving: boolean;

    movies: Movie[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private voteService: VoteService,
        private movieService: MovieService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.movieService.query()
            .subscribe((res: HttpResponse<Movie[]>) => { this.movies = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vote.id !== undefined) {
            this.subscribeToSaveResponse(
                this.voteService.update(this.vote));
        } else {
            this.subscribeToSaveResponse(
                this.voteService.create(this.vote));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Vote>>) {
        result.subscribe((res: HttpResponse<Vote>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Vote) {
        this.eventManager.broadcast({ name: 'voteListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-vote-popup',
    template: ''
})
export class VotePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private votePopupService: VotePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.votePopupService
                    .open(VoteDialogComponent as Component, params['id']);
            } else {
                this.votePopupService
                    .open(VoteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
