import {Component, OnInit} from '@angular/core';
import {Movie} from '../../entities/movie';
import {Vote, VoteService} from '../../entities/vote';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {JhiAlertService} from 'ng-jhipster';

@Component({
    selector: 'jhi-fury-contest',
    templateUrl: './fury-contest.component.html',
    styleUrls: [
        'fury-contest.scss'
    ]
})
export class FuryContestComponent implements OnInit {

    leftMovie: Movie;
    rightMovie: Movie;
    fetching = false;

    constructor(
        private voteService: VoteService,
        private jhiAlertService: JhiAlertService,
    ) {
    }

    ngOnInit() {
    }

    movieClicked(movie: Movie) {
        const vote = new Vote();
        if (movie === this.leftMovie) {
            vote.winnerId = this.leftMovie.id;
            vote.loserId = this.rightMovie.id;
        } else {
            vote.winnerId = this.rightMovie.id;
            vote.loserId = this.leftMovie.id;
        }

        // here trigger selection animation
        this.fetching = true;

        this.voteService.fury(vote)
            .subscribe(
                (res: HttpResponse<Vote>) => {
                    this.fetching = false;
                },
                (res: HttpErrorResponse) => {
                    this.fetching = false;
                    this.jhiAlertService.error(res.message, null, null);
                }
            );
    }
}
