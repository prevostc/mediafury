import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Vote } from './vote.model';
import { VotePopupService } from './vote-popup.service';
import { VoteService } from './vote.service';

@Component({
    selector: 'jhi-vote-delete-dialog',
    templateUrl: './vote-delete-dialog.component.html'
})
export class VoteDeleteDialogComponent {

    vote: Vote;

    constructor(
        private voteService: VoteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.voteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'voteListModification',
                content: 'Deleted an vote'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vote-delete-popup',
    template: ''
})
export class VoteDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private votePopupService: VotePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.votePopupService
                .open(VoteDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
