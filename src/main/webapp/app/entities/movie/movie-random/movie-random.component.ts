import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {MovieService} from '../movie.service';
import {Movie} from '../movie.model';
import {HttpResponse} from '@angular/common/http';

@Component({
  selector: 'jhi-movie-random',
  templateUrl: './movie-random.component.html',
  styles: []
})
export class MovieRandomComponent implements OnInit {

    movie: Movie;
    @Output() movieChange = new EventEmitter<Movie>();
    @Output() onClick = new EventEmitter<Movie>();

    constructor(
        private movieService: MovieService,
    ) {
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
