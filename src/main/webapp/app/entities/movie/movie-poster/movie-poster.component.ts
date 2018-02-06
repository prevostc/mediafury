import { Component, Input } from '@angular/core';
import {Movie} from '../movie.model';

@Component({
  selector: 'jhi-movie-poster',
  templateUrl: './movie-poster.component.html',
  styles: []
})
export class MoviePosterComponent {
    @Input() movie: Movie;
    @Input() height = 300;

    getImageUrl(): string {
        return this.movie.imageUrl ? this.movie.imageUrl : 'http://via.placeholder.com/300x445?text=N/A';
    }
}
