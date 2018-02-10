import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MediafurySharedModule } from '../../shared';
import {
    MovieService,
    MoviePopupService,
    MovieComponent,
    MovieDetailComponent,
    MovieDialogComponent,
    MoviePopupComponent,
    MovieRandomComponent,
    MovieDeletePopupComponent,
    MovieDeleteDialogComponent,
    movieRoute,
    moviePopupRoute,
    MovieResolvePagingParams,
} from './';
import { MoviePosterComponent } from './movie-poster/movie-poster.component';
import {NgSelectModule} from '@ng-select/ng-select';

const ENTITY_STATES = [
    ...movieRoute,
    ...moviePopupRoute,
];

@NgModule({
    imports: [
        MediafurySharedModule,
        NgSelectModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MovieComponent,
        MovieDetailComponent,
        MovieDialogComponent,
        MovieDeleteDialogComponent,
        MoviePopupComponent,
        MovieDeletePopupComponent,
        MovieRandomComponent,
        MoviePosterComponent,
    ],
    entryComponents: [
        MovieComponent,
        MovieDialogComponent,
        MoviePopupComponent,
        MovieDeleteDialogComponent,
        MovieDeletePopupComponent,
    ],
    providers: [
        MovieService,
        MoviePopupService,
        MovieResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports: [
        MovieRandomComponent,
        MoviePosterComponent
    ],
})
export class MediafuryMovieModule {}
