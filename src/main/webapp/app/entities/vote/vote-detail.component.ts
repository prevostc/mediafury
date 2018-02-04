import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Vote } from './vote.model';
import { VoteService } from './vote.service';

@Component({
    selector: 'jhi-vote-detail',
    templateUrl: './vote-detail.component.html'
})
export class VoteDetailComponent implements OnInit, OnDestroy {

    vote: Vote;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private voteService: VoteService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVotes();
    }

    load(id) {
        this.voteService.find(id)
            .subscribe((voteResponse: HttpResponse<Vote>) => {
                this.vote = voteResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVotes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'voteListModification',
            (response) => this.load(this.vote.id)
        );
    }
}
