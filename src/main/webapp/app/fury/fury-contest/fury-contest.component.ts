import {Component, OnInit} from '@angular/core';
import {Movie} from '../../entities/movie';

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

    constructor() {
    }

    ngOnInit() {
    }

    movieClicked(movie: Movie) {
        if (movie === this.leftMovie) {
            console.log('Left movie clicked', this.leftMovie);
        } else {
            console.log('Right movie clicked', this.rightMovie);
        }
    }
}
