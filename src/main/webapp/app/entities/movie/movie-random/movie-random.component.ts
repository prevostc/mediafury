import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {MovieService} from '../movie.service';
import {Movie} from '../movie.model';
import {HttpResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-movie-random',
    templateUrl: './movie-random.component.html',
    styleUrls: [
        'movie-random.scss'
    ]
})
export class MovieRandomComponent implements OnInit, OnChanges {

    @Input() @Output() movie;
    @Output() movieChange = new EventEmitter<Movie>();
    @Output() onClick = new EventEmitter<Movie>();

    constructor(
        private movieService: MovieService,
    ) {
    }

    ngOnChanges(change: SimpleChanges) {
        if (! change.movie.firstChange && change.movie.currentValue === null) {
            this.load();
        }
    }

    ngOnInit() {
        this.load();
    }

    load() {
        this.movieService.random()
            .subscribe((movieResponse: HttpResponse<Movie>) => {
                this.movie = movieResponse.body;
                this.movieChange.emit(this.movie);
            });
    }

    clicked() {
        this.onClick.emit(this.movie);
    }

}
