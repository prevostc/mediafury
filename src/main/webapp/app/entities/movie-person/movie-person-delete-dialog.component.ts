import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MoviePerson } from './movie-person.model';
import { MoviePersonPopupService } from './movie-person-popup.service';
import { MoviePersonService } from './movie-person.service';

@Component({
    selector: 'jhi-movie-person-delete-dialog',
    templateUrl: './movie-person-delete-dialog.component.html'
})
export class MoviePersonDeleteDialogComponent {

    moviePerson: MoviePerson;

    constructor(
        private moviePersonService: MoviePersonService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.moviePersonService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'moviePersonListModification',
                content: 'Deleted an moviePerson'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-movie-person-delete-popup',
    template: ''
})
export class MoviePersonDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private moviePersonPopupService: MoviePersonPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.moviePersonPopupService
                .open(MoviePersonDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
