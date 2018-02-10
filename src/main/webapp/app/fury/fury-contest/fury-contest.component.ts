import {Component, EventEmitter, OnInit} from '@angular/core';
import {Movie} from '../../entities/movie';
import {Vote, VoteService} from '../../entities/vote';
import {JhiAlertService} from 'ng-jhipster';
import {trigger, style, transition, animate, state} from '@angular/animations';
import { zip } from 'rxjs/observable/zip';
import { AnimationEvent } from '@angular/animations';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';

enum FuryMovieState {
    FETCHING = 'fetching',
    DISPLAYED = 'displayed',
    WON = 'won',
    LOST = 'lost',
}

@Component({
    selector: 'jhi-fury-contest',
    templateUrl: './fury-contest.component.html',
    styleUrls: [
        'fury-contest.scss'
    ],
    animations: [
        trigger('movieState', [
            state(FuryMovieState.DISPLAYED, style({
                transform: 'scale(1)',
            })),
            state(FuryMovieState.WON, style({
                opacity: 1,
                transform: 'scale(1.2)',
            })),
            state(FuryMovieState.LOST, style({
                opacity: 0.3,
                transform: 'scale(0.4)',
            })),

            transition(`* => ${FuryMovieState.DISPLAYED}`, animate('100ms ease-in')),
            transition(`* => ${FuryMovieState.LOST}`, animate('200ms ease-out')),
            transition(`* => ${FuryMovieState.WON}`, animate('200ms ease-out')),
            transition(`* => ${FuryMovieState.FETCHING}`, animate('200ms ease-out')),
        ]),
    ]
})
export class FuryContestComponent implements OnInit {

    leftMovie: Movie;
    rightMovie: Movie;
    leftMovieState: FuryMovieState;
    rightMovieState: FuryMovieState;

    movieClicked$ = new EventEmitter<Vote>();
    animation$ = new EventEmitter<void>();

    constructor(
        private voteService: VoteService,
        private jhiAlertService: JhiAlertService,
    ) {
        this.rightMovieState = FuryMovieState.FETCHING;
        this.leftMovieState = FuryMovieState.FETCHING;

        // when both animation is ended and vote object has been built
        // then call the vote service
        zip(this.movieClicked$, this.animation$)
            .switchMap(([vote, _]) => {
                return this.voteService.fury(vote);
            })
            .subscribe(
                (res: HttpResponse<Vote>) => {
                    // reset movie state
                    this.leftMovie = null;
                    this.rightMovie = null;
                    this.rightMovieState = FuryMovieState.FETCHING;
                    this.leftMovieState = FuryMovieState.FETCHING;
                },
                (res: HttpErrorResponse) => {
                    this.jhiAlertService.error(res.message, null, null);
                }
            )
        ;
    }

    ngOnInit() {
    }

    movieClicked(movie: Movie) {
        const vote = new Vote();

        if (this.leftMovieState !== FuryMovieState.DISPLAYED || this.rightMovieState !== FuryMovieState.DISPLAYED) {
            return;
        }

        if (movie === this.leftMovie) {
            vote.winnerId = this.leftMovie.id;
            vote.loserId = this.rightMovie.id;
            this.leftMovieState = FuryMovieState.WON;
            this.rightMovieState = FuryMovieState.LOST;
        } else {
            vote.winnerId = this.rightMovie.id;
            vote.loserId = this.leftMovie.id;
            this.rightMovieState = FuryMovieState.WON;
            this.leftMovieState = FuryMovieState.LOST;
        }

        this.movieClicked$.emit(vote);
    }

    animDone(event: AnimationEvent) {
        if (event.fromState === FuryMovieState.DISPLAYED && (
            event.toState === FuryMovieState.LOST || event.toState === FuryMovieState.WON
        )) {
            this.animation$.emit();
        }
    }

    gotLeftMovie(movie: Movie) {
        this.leftMovie = movie;
        this.leftMovieState = FuryMovieState.DISPLAYED;
    }
    gotRightMovie(movie: Movie) {
        this.rightMovie = movie;
        this.rightMovieState = FuryMovieState.DISPLAYED;
    }
}
